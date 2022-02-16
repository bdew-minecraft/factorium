package net.bdew.advtech.machines.processing

import net.bdew.advtech.machines.BaseMachineEntity
import net.bdew.advtech.misc.{AutoIOMode, DataSlotItemQueue}
import net.bdew.advtech.upgrades._
import net.bdew.advtech.upgrades.upgradable.{InfoEntry, InfoEntryKind, UpgradeableMachine}
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.handlers.{PowerEnergyHandler, SidedInventoryItemHandler}
import net.bdew.lib.data.base.{DataSlot, UpdateKind}
import net.bdew.lib.data.{DataSlotEnum, DataSlotFloat, DataSlotInt, DataSlotInventory}
import net.bdew.lib.inventory.RestrictedInventory
import net.bdew.lib.items.ItemUtils
import net.bdew.lib.misc.RSMode
import net.bdew.lib.power.DataSlotPower
import net.bdew.lib.{DecFormat, Misc, Text}
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.items.IItemHandler

abstract class ProcessingMachineEntity(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends BaseMachineEntity(teType, pos, state) with UpgradeableMachine {
  object Slots {
    val input: Range = 0 to 5
    val output: Range = 6 to 11
  }

  def config: ProcessingMachineConfig
  def recipes: Set[_ <: ProcessingRecipe]

  var haveWork = true
  var needPower = true

  val inventory: DataSlotInventory = DataSlotInventory("inv", this, 18)
  val upgrades: DataSlotUpgrades = new DataSlotUpgrades(this)
  val outputQueue: DataSlotItemQueue = DataSlotItemQueue("outputQueue", this)
  val power: DataSlotPower = DataSlotPower("power", this)
  val rsMode: DataSlotEnum[RSMode.type] = DataSlotEnum("rsMode", this, RSMode)
  val ioMode: DataSlotEnum[AutoIOMode.type] = DataSlotEnum("ioMode", this, AutoIOMode)

  val progress: DataSlotFloat = DataSlotFloat("progress", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val workSpeed: DataSlotFloat = DataSlotFloat("workSpeed", this).setUpdate(UpdateKind.GUI)
  val powerUse: DataSlotFloat = DataSlotFloat("powerUse", this).setUpdate(UpdateKind.GUI)
  val parallelProcess: DataSlotInt = DataSlotInt("parallelProcess", this).setUpdate(UpdateKind.GUI)

  val externalInventory = new RestrictedInventory(inventory,
    canExtract = (slot, _) => Slots.output.contains(slot),
    canInsert = (slot, stack, _) => Slots.input.contains(slot) && isValidInput(stack)
  )

  val inventoryHandler: LazyOptional[IItemHandler] = SidedInventoryItemHandler.create(externalInventory)
  val powerHandler: LazyOptional[IEnergyStorage] = PowerEnergyHandler.create(power, true, false)

  serverTick.listen(doTick)

  def doTick(): Unit = {
    if (upgrades.needsUpdate) {
      workSpeed := upgrades.calculate(UpgradeStat.WorkSpeed, 1 / config.baseCycleTicks())
      powerUse := upgrades.calculate(UpgradeStat.EnergyConsumption, config.basePowerUsage())
      parallelProcess := upgrades.calculate(UpgradeStat.ParallelProcess, 1)
      power.configure(
        capacity = upgrades.calculate(UpgradeStat.EnergyCapacity, config.basePowerCapacity()),
        maxReceive = upgrades.calculate(UpgradeStat.ChargeRate, config.baseChargingRate())
      )
      upgrades.needsUpdate = false
    }

    if (!haveWork) return

    if (outputQueue.nonEmpty) {
      processOutputQueue()
      if (outputQueue.isEmpty) {
        progress -= 1
      } else {
        haveWork = false
        return
      }
    }

    if (!canWorkRS) {
      haveWork = false
      return
    }

    if (power.stored < powerUse) {
      needPower = true
      haveWork = false
      return
    }

    if (Slots.input.map(inventory.getItem).forall(_.isEmpty)) {
      if (progress > 0) progress := 0
      haveWork = false
      return
    }

    power.extract(powerUse, false)
    progress += workSpeed

    if (progress >= 1) {
      processRecipes()
    }
  }

  def processRecipes(): Unit = {
    var remaining = parallelProcess.value
    for {
      slot <- Slots.input if remaining > 0
      stack = inventory.getItem(slot) if !stack.isEmpty
      recipe <- recipes.find(_.input.test(stack))
    } {
      val count = Misc.min(stack.getCount, remaining)
      inventory.removeItem(slot, count)
      val outputs = recipe.output.roll(count)
      val secondary = recipe.secondary.roll(count)
      val bonus = recipe.bonus.roll(count)
      if (!outputs.isEmpty) outputQueue.push(outputs)
      if (!secondary.isEmpty) outputQueue.push(secondary)
      if (!bonus.isEmpty) outputQueue.push(bonus)
      remaining -= count
    }
  }

  def canWorkRS: Boolean = rsMode.value match {
    case RSMode.ALWAYS => true
    case RSMode.NEVER => false
    case RSMode.RS_ON => level.hasNeighborSignal(worldPosition)
    case RSMode.RS_OFF => !level.hasNeighborSignal(worldPosition)
  }

  def processOutputQueue(): Unit = {
    while (outputQueue.nonEmpty) {
      var stack = outputQueue.pop()
      stack = ItemUtils.addStackToSlots(stack, inventory, Slots.output, false)
      if (!stack.isEmpty) {
        outputQueue.push(stack)
        return
      }
    }
  }

  def isValidInput(stack: ItemStack): Boolean = recipes.exists(_.input.test(stack))

  override def dataSlotChanged(slot: DataSlot): Unit = {
    if (needPower && slot == power) {
      haveWork = true
      needPower = false
    } else if (!haveWork && slot == inventory) {
      haveWork = true
    } else if (!haveWork && slot == rsMode) {
      haveWork = true
    }
    super.dataSlotChanged(slot)
  }

  override def doSave(kind: UpdateKind.Value, t: CompoundTag): Unit = {
    super.doSave(kind, t)
    if (kind == UpdateKind.GUI && t.getFloat(progress.name) > 1)
      t.putFloat(progress.name, 1f)
  }

  override def statsDisplay(line: Int): Option[InfoEntry] = line match {
    case 0 => InfoEntryKind.CycleLength.value(Text.translate("bdlib.format.amount.unit", DecFormat.dec2(1 / workSpeed / 20), Text.unit("seconds")))
    case 1 => InfoEntryKind.EnergyUsed.value(Text.energyPerTick(powerUse))
    case 2 if parallelProcess > 1 => InfoEntryKind.ItemsPerCycle.value(Text.string(DecFormat.round(parallelProcess)))
    case _ => None
  }

  //noinspection ComparingUnrelatedTypes
  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == Capabilities.CAP_ITEM_HANDLER)
      inventoryHandler.cast()
    else if (cap == Capabilities.CAP_ENERGY_HANDLER)
      powerHandler.cast()
    else
      super.getCapability(cap, side)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    powerHandler.invalidate()
    inventoryHandler.invalidate()
  }
}
