package net.bdew.advtech.machines.alloy

import net.bdew.advtech.Config
import net.bdew.advtech.machines.MachineRecipes
import net.bdew.advtech.machines.upgradable.{InfoEntry, InfoEntryKind}
import net.bdew.advtech.machines.worker.WorkerMachineEntity
import net.bdew.advtech.registries.Blocks
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.handlers.{PowerEnergyHandler, SidedInventoryItemHandler}
import net.bdew.lib.data.DataSlotInventory
import net.bdew.lib.inventory.RestrictedInventory
import net.bdew.lib.items.ItemUtils
import net.bdew.lib.{DecFormat, Text}
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.items.IItemHandler

class AlloySmelterEntity(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends WorkerMachineEntity(teType, pos, state) {
  object Slots {
    val input: Range = 0 to 1
    val output: Range = 2 to 7
  }

  override def config: AlloySmelterConfig = Config.Machines.AlloySmelter
  def recipes: Set[AlloyRecipe] = MachineRecipes.alloy

  val inventory: DataSlotInventory = DataSlotInventory("inv", this, 18)

  val externalInventory = new RestrictedInventory(inventory,
    canExtract = (slot, _) => Slots.output.contains(slot),
    canInsert = (slot, stack, _) => Slots.input.contains(slot) && isValidInput(stack, slot)
  )

  val inventoryHandler: LazyOptional[IItemHandler] = SidedInventoryItemHandler.create(externalInventory)
  val powerHandler: LazyOptional[IEnergyStorage] = PowerEnergyHandler.create(power, true, false)

  def isValidInput(stack: ItemStack, slot: Int): Boolean =
    (slot == 0 || slot == 1) && recipes.exists(_.testSoft(stack, inventory.getItem(1 - slot)))

  def testRecipe(rec: AlloyRecipe): Boolean = rec.test(inventory.getItem(0), inventory.getItem(1))

  var cachedRecipe: Option[AlloyRecipe] = None

  def findRecipe: Option[AlloyRecipe] = {
    cachedRecipe match {
      case Some(rec) if testRecipe(rec) => cachedRecipe
      case _ =>
        cachedRecipe = recipes.find(testRecipe)
        cachedRecipe
    }
  }

  override def getWorkSpeed: Float = super.getWorkSpeed * cachedRecipe.map(_.speedMod).getOrElse(0F)

  override def haveValidInputs: Boolean = findRecipe.isDefined

  override def processRecipes(): Unit = {
    for {
      recipe <- findRecipe
      _ <- 0 until parallelProcess.value if testRecipe(recipe)
    } {
      if (recipe.input1.test(inventory.getItem(0))) {
        inventory.removeItem(0, recipe.input1.count)
        inventory.removeItem(1, recipe.input2.count)
      } else {
        inventory.removeItem(0, recipe.input2.count)
        inventory.removeItem(1, recipe.input1.count)
      }
      outputQueue.push(recipe.output.copy())
    }
  }

  override def addToOutputs(stack: ItemStack): ItemStack =
    ItemUtils.addStackToSlots(stack, inventory, Slots.output, false)

  override def statsDisplay(line: Int): Option[InfoEntry] = line match {
    case 0 => InfoEntryKind.CycleLength.value(Text.translate("bdlib.format.amount.unit", DecFormat.dec2(1 / workSpeed / 20), Text.unit("seconds")))
    case 1 => InfoEntryKind.EnergyUsed.value(Text.energyPerTick(powerUse))
    case 2 if parallelProcess > 1 => InfoEntryKind.ItemsPerCycle.value(Text.string(DecFormat.round(parallelProcess)))
    case _ => None
  }

  override def getDisplayName: Component = Text.translate(Blocks.alloySmelter.block.get().getDescriptionId)
  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new AlloySmelterContainer(this, playerInventory, id)

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
