package net.bdew.factorium.machines.pump

import net.bdew.factorium.Config
import net.bdew.factorium.machines.BaseMachineEntity
import net.bdew.factorium.machines.upgradable.{DataSlotUpgrades, InfoEntry, InfoEntryKind, UpgradeableMachine}
import net.bdew.factorium.registries.Blocks
import net.bdew.factorium.upgrades.{UpgradeClass, UpgradeStat}
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.handlers.PowerEnergyHandler
import net.bdew.lib.capabilities.helpers.fluid.RestrictedFluidHandler
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.data.{DataSlotEnum, DataSlotFloat, DataSlotTank}
import net.bdew.lib.misc.RSMode
import net.bdew.lib.power.DataSlotPower
import net.bdew.lib.{DecFormat, Text}
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.{Blocks => MCBlocks}
import net.minecraft.world.level.material.{Fluid, Fluids}
import net.minecraft.world.phys.AABB
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction

import scala.collection.mutable

class PumpEntity(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends BaseMachineEntity(teType, pos, state) with UpgradeableMachine {
  def config: PumpConfig = Config.Machines.Pump

  val power: DataSlotPower = DataSlotPower("power", this)
  val tank: DataSlotTank = DataSlotTank("tank", this, config.tankCapacity())
  val rsMode: DataSlotEnum[RSMode.type] = DataSlotEnum("rsMode", this, RSMode)
  val upgrades: DataSlotUpgrades = new DataSlotUpgrades(this)

  val progress: DataSlotFloat = DataSlotFloat("progress", this).setUpdate(UpdateKind.SAVE, UpdateKind.GUI)
  val workSpeed: DataSlotFloat = DataSlotFloat("workSpeed", this).setUpdate(UpdateKind.GUI)
  val powerUse: DataSlotFloat = DataSlotFloat("powerUse", this).setUpdate(UpdateKind.GUI)

  val pumpState = new DataSlotPumpState("state", this)

  val powerHandler: LazyOptional[IEnergyStorage] = PowerEnergyHandler.create(power, true, false)
  val fluidHandler: LazyOptional[IFluidHandler] = LazyOptional.of(() => RestrictedFluidHandler.drainOnly(tank))

  private val scanQueue = mutable.Queue.empty[BlockPos]
  private val blockQueue = mutable.Queue.empty[BlockPos]
  private val seenBlocks = mutable.Set.empty[BlockPos]

  override def validUpgradeClasses: Set[UpgradeClass] = Set(UpgradeClass.Core, UpgradeClass.SpeedEfficiency)

  serverTick.listen(doTick)

  def doTick(): Unit = {
    if (upgrades.needsUpdate) {
      workSpeed := upgrades.calculate(UpgradeStat.WorkSpeed, 1 / config.baseCycleTicks())
      powerUse := upgrades.calculate(UpgradeStat.EnergyConsumption, config.basePowerUsage())
      power.configure(
        capacity = upgrades.calculate(UpgradeStat.EnergyCapacity, config.basePowerCapacity()),
        maxReceive = upgrades.calculate(UpgradeStat.ChargeRate, config.baseChargingRate())
      )
      upgrades.needsUpdate = false
    }

    if (!canWorkRS || power.stored < powerUse || tank.getCapacity - tank.getFluid.getAmount < 1000) return

    pumpState.get match {
      case PumpState.Invalid => return
      case PumpState.Lowering(toDepth) =>
        pumpState.change(startPumpingAt(toDepth))
      case PumpState.Scanning(atDepth, fluid) =>
        if (scanQueue.isEmpty) {
          startPumpingAt(atDepth)
        } else doScan(atDepth, fluid)
      case PumpState.Pumping(atDepth, infinite, fluid) =>
        val currentPos: BlockPos = worldPosition.below(atDepth)
        val currentFluid = level.getFluidState(currentPos)
        if (currentFluid.is(fluid) && (infinite || blockQueue.nonEmpty)) {
          if (progress < 1) {
            progress += workSpeed
            power.extract(powerUse, false)
          }
          if (progress >= 1) {
            if (tank.fill(new FluidStack(fluid, 1000), FluidAction.SIMULATE) != 1000) return
            if (!infinite && !consumeNextBlock(fluid)) return
            tank.fill(new FluidStack(currentFluid.getType, 1000), FluidAction.EXECUTE)
            progress -= 1
          }
        } else {
          pumpState.change(startPumpingAt(atDepth))
        }
    }
  }

  def consumeNextBlock(fluid: Fluid): Boolean = {
    val pos = blockQueue.dequeue()
    val state = level.getFluidState(pos)
    if (state.isEmpty || !state.isSource || !state.getType.isSame(fluid)) return false
    level.setBlock(pos, MCBlocks.AIR.defaultBlockState(), 3)
    true
  }

  def startPumpingAt(depth: Int): PumpState = {
    progress := 0
    val nextPos: BlockPos = worldPosition.below(depth)
    if (nextPos.getY < level.getMinBuildHeight) {
      PumpState.Invalid
    } else if (level.getFluidState(nextPos).isEmpty) {
      if (level.getBlockState(nextPos).isAir) {
        PumpState.Lowering(depth + 1)
      } else {
        PumpState.Invalid
      }
    } else {
      val fluid = level.getFluidState(nextPos)
      if (fluid.getType == Fluids.WATER) {
        var sources = 0
        if (level.getFluidState(nextPos.east()).isSourceOfType(Fluids.WATER)) sources += 1
        if (level.getFluidState(nextPos.west()).isSourceOfType(Fluids.WATER)) sources += 1
        if (level.getFluidState(nextPos.north()).isSourceOfType(Fluids.WATER)) sources += 1
        if (level.getFluidState(nextPos.south()).isSourceOfType(Fluids.WATER)) sources += 1
        if (sources >= 2) return PumpState.Pumping(depth, true, Fluids.WATER)
      }
      queueScan(nextPos)
      PumpState.Scanning(depth, fluid.getType)
    }
  }

  def checkBlock(nextPos: BlockPos, fluid: Fluid): Boolean = (
    Math.abs(nextPos.getX - worldPosition.getX) <= config.maxPumpDistance()
      && Math.abs(nextPos.getZ - worldPosition.getZ) <= config.maxPumpDistance()
      && level.getFluidState(nextPos).getType.isSame(fluid)
    )

  def queueScan(blockPos: BlockPos): Unit = {
    if (!seenBlocks.contains(blockPos)) {
      scanQueue.enqueue(blockPos)
      seenBlocks.add(blockPos)
    }
  }

  def doScan(depth: Int, fluid: Fluid): Unit = {
    var remaining = config.scanPerTick()
    while (remaining > 0 && scanQueue.nonEmpty) {
      val nextPos = scanQueue.dequeue()

      if (checkBlock(nextPos, fluid)) {
        if (level.getFluidState(nextPos).isSource)
          blockQueue.prepend(nextPos)

        if (blockQueue.size >= config.infiniteThreshold()) {
          pumpState.change(PumpState.Pumping(depth, true, fluid))
          scanQueue.clear()
          blockQueue.clear()
          seenBlocks.clear()
          return
        }

        queueScan(nextPos.north())
        queueScan(nextPos.east())
        queueScan(nextPos.south())
        queueScan(nextPos.west())
      }

      remaining -= 1
    }

    if (scanQueue.isEmpty) {
      if (blockQueue.nonEmpty) {
        seenBlocks.clear()
        pumpState.change(PumpState.Pumping(depth, false, fluid))
      } else {
        seenBlocks.clear()
        pumpState.change(PumpState.Lowering(depth + 1))
      }
    }
  }

  override def getDisplayName: Component = Text.translate(Blocks.pump.block.get().getDescriptionId)

  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new PumpContainer(this, playerInventory, id)

  override def statsDisplay(line: Int): Option[InfoEntry] = line match {
    case 0 => InfoEntryKind.CycleLength.value(Text.translate("bdlib.format.amount.unit", DecFormat.dec2(1 / workSpeed / 20), Text.unit("seconds")))
    case 1 => InfoEntryKind.EnergyUsed.value(Text.energyPerTick(powerUse))
    case _ => None
  }

  def canWorkRS: Boolean = rsMode.value match {
    case RSMode.ALWAYS => true
    case RSMode.NEVER => false
    case RSMode.RS_ON => level.hasNeighborSignal(worldPosition)
    case RSMode.RS_OFF => !level.hasNeighborSignal(worldPosition)
  }

  //noinspection ComparingUnrelatedTypes
  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == Capabilities.CAP_FLUID_HANDLER)
      fluidHandler.cast()
    else if (cap == Capabilities.CAP_ENERGY_HANDLER)
      powerHandler.cast()
    else
      super.getCapability(cap, side)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    powerHandler.invalidate()
    fluidHandler.invalidate()
  }


  override def afterTileBreakSave(t: CompoundTag): CompoundTag = {
    t.remove(pumpState.name)
    t.remove(progress.name)
    t
  }

  override def getRenderBoundingBox: AABB = {
    new AABB(pos, pos.offset(1, -hoseLength, 1): BlockPos)
  }

  def hoseLength: Int = pumpState.get match {
    case PumpState.Invalid => 0
    case PumpState.Lowering(d) => d
    case PumpState.Pumping(d, _, _) => d
    case PumpState.Scanning(d, _) => d
  }
}
