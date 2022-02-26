package net.bdew.factorium.machines.processing

import net.bdew.factorium.machines.sided.SidedItemIOEntity
import net.bdew.factorium.machines.upgradable.{InfoEntry, InfoEntryKind, UpgradeableMachine}
import net.bdew.factorium.machines.worker.WorkerMachineEntity
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.handlers.{PowerEnergyHandler, SidedInventoryItemHandler}
import net.bdew.lib.data.DataSlotInventory
import net.bdew.lib.inventory.RestrictedInventory
import net.bdew.lib.items.ItemUtils
import net.bdew.lib.{DecFormat, Misc, Text}
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.items.IItemHandler

abstract class ProcessingMachineEntity(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends WorkerMachineEntity(teType, pos, state) with UpgradeableMachine with SidedItemIOEntity {
  object Slots {
    val input: Range = 0 to 5
    val output: Range = 6 to 11
  }

  def recipes: Set[_ <: ProcessingRecipe]

  val inventory: DataSlotInventory = DataSlotInventory("inv", this, 18)

  val externalInventory = new RestrictedInventory(inventory,
    canExtract = (slot, _) => Slots.output.contains(slot),
    canInsert = (slot, stack, _) => Slots.input.contains(slot) && isValidInput(stack)
  )

  val inventoryHandler: LazyOptional[IItemHandler] = SidedInventoryItemHandler.create(externalInventory)
  val powerHandler: LazyOptional[IEnergyStorage] = PowerEnergyHandler.create(power, true, false)

  override def processRecipes(): Unit = {
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

  def isValidInput(stack: ItemStack): Boolean = recipes.exists(_.input.test(stack))
  override def haveValidInputs: Boolean = !Slots.input.map(inventory.getItem).forall(_.isEmpty)
  override def addToOutputs(stack: ItemStack): ItemStack =
    ItemUtils.addStackToSlots(stack, inventory, Slots.output, false)

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


