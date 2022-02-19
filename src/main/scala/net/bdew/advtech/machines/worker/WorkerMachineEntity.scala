package net.bdew.advtech.machines.worker

import net.bdew.advtech.machines.sided.SidedItemIOEntity
import net.bdew.advtech.machines.upgradable.{DataSlotUpgrades, UpgradeableMachine}
import net.bdew.advtech.machines.{BaseMachineEntity, RotatableMachineBlock}
import net.bdew.advtech.misc.{AutoIOMode, DataSlotItemQueue}
import net.bdew.advtech.upgrades.UpgradeStat
import net.bdew.lib.data.base.{DataSlot, UpdateKind}
import net.bdew.lib.data.{DataSlotEnum, DataSlotFloat, DataSlotInt, DataSlotInventory}
import net.bdew.lib.misc.RSMode
import net.bdew.lib.power.DataSlotPower
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class WorkerMachineEntity(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends BaseMachineEntity(teType, pos, state) with UpgradeableMachine with SidedItemIOEntity {
  def config: WorkerMachineConfig

  var haveWork = true
  var needPower = true

  def inventory: DataSlotInventory

  val upgrades: DataSlotUpgrades = new DataSlotUpgrades(this)
  val outputQueue: DataSlotItemQueue = DataSlotItemQueue("outputQueue", this)
  val power: DataSlotPower = DataSlotPower("power", this)
  val rsMode: DataSlotEnum[RSMode.type] = DataSlotEnum("rsMode", this, RSMode)
  val ioMode: DataSlotEnum[AutoIOMode.type] = DataSlotEnum("ioMode", this, AutoIOMode)

  val progress: DataSlotFloat = DataSlotFloat("progress", this).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)
  val workSpeed: DataSlotFloat = DataSlotFloat("workSpeed", this).setUpdate(UpdateKind.GUI)
  val powerUse: DataSlotFloat = DataSlotFloat("powerUse", this).setUpdate(UpdateKind.GUI)
  val parallelProcess: DataSlotInt = DataSlotInt("parallelProcess", this).setUpdate(UpdateKind.GUI)

  override def getFacing: Direction = getBlockState.getBlock.asInstanceOf[RotatableMachineBlock].getFacing(getBlockState)

  def processRecipes(): Unit
  def haveValidInputs: Boolean
  def addToOutputs(stack: ItemStack): ItemStack

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

    if (!haveValidInputs) {
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


  def canWorkRS: Boolean = rsMode.value match {
    case RSMode.ALWAYS => true
    case RSMode.NEVER => false
    case RSMode.RS_ON => level.hasNeighborSignal(worldPosition)
    case RSMode.RS_OFF => !level.hasNeighborSignal(worldPosition)
  }

  def processOutputQueue(): Unit = {
    while (outputQueue.nonEmpty) {
      var stack = outputQueue.pop()
      stack = addToOutputs(stack)
      if (!stack.isEmpty) {
        outputQueue.push(stack)
        return
      }
    }
  }

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
}
