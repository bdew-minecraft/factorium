package net.bdew.factorium.machines.mixer

import net.bdew.factorium.Config
import net.bdew.factorium.machines.MachineRecipes
import net.bdew.factorium.machines.upgradable.{InfoEntry, InfoEntryKind}
import net.bdew.factorium.machines.worker.WorkerMachineEntity
import net.bdew.factorium.registries.Blocks
import net.bdew.factorium.upgrades.UpgradeClass
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.handlers.{PowerEnergyHandler, SidedInventoryItemHandler}
import net.bdew.lib.capabilities.helpers.fluid.{FluidMultiHandler, RestrictedFluidHandler}
import net.bdew.lib.data.{DataSlotInventory, DataSlotTank}
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
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction
import net.minecraftforge.items.IItemHandler

class MixerEntity(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends WorkerMachineEntity(teType, pos, state) {
  object Slots {
    val input = 0
    val output = 1
  }

  override def config: MixerConfig = Config.Machines.Mixer
  def recipes: Set[MixerRecipe] = MachineRecipes.mixer

  override def validUpgradeClasses: Set[UpgradeClass] =
    Set(UpgradeClass.Core, UpgradeClass.SpeedEfficiency, UpgradeClass.Parallel)

  val inventory: DataSlotInventory = DataSlotInventory("inv", this, 18)

  val externalInventory = new RestrictedInventory(inventory,
    canExtract = (slot, _) => slot == Slots.output,
    canInsert = (slot, stack, _) => slot == Slots.input && isValidInput(stack)
  )

  val inputTank = new DataSlotTank("inputTank", this, config.tankCapacity())
  val outputTank = new DataSlotTank("outputTank", this, config.tankCapacity())

  val inventoryHandler: LazyOptional[IItemHandler] = SidedInventoryItemHandler.create(externalInventory)
  val powerHandler: LazyOptional[IEnergyStorage] = PowerEnergyHandler.create(power, true, false)
  val fluidHandler: LazyOptional[IFluidHandler] = LazyOptional.of(() =>
    FluidMultiHandler.wrap(
      new RestrictedFluidHandler(inputTank, isValidInput, false),
      RestrictedFluidHandler.drainOnly(outputTank),
    )
  )

  def isValidInput(stack: ItemStack): Boolean = recipes.exists(_.testSoft(stack, inputTank.getFluid))
  def isValidInput(stack: FluidStack): Boolean = recipes.exists(_.testSoft(inventory.getItem(Slots.input), stack))

  def testRecipe(rec: MixerRecipe): Boolean = rec.test(inventory.getItem(Slots.input), inputTank.getFluid)

  var cachedRecipe: Option[MixerRecipe] = None
  def findRecipe: Option[MixerRecipe] = {
    cachedRecipe match {
      case Some(rec) if testRecipe(rec) => cachedRecipe
      case _ =>
        cachedRecipe = recipes.find(testRecipe)
        cachedRecipe
    }
  }

  def canOutput(rec: MixerRecipe): Boolean =
    rec.outputFluid.isEmpty || outputTank.fill(rec.outputFluid.toStack, FluidAction.SIMULATE) == rec.outputFluid.amount

  override def haveValidInputs: Boolean = findRecipe.isDefined

  override def processRecipes(): Unit = {
    for {
      recipe <- findRecipe
      _ <- 0 until parallelProcess.value if testRecipe(recipe) && canOutput(recipe)
    } {
      inventory.removeItem(Slots.input, recipe.inputItem.count)
      inputTank.drain(recipe.inputFluid.amount, FluidAction.EXECUTE)
      if (!recipe.outputItem.isEmpty)
        outputQueue.push(recipe.outputItem.copy())
      if (!recipe.outputFluid.isEmpty)
        outputTank.fill(recipe.outputFluid.toStack, FluidAction.EXECUTE)
    }
  }

  override def addToOutputs(stack: ItemStack): ItemStack =
    ItemUtils.addStackToSlots(stack, inventory, List(Slots.output), false)

  override def statsDisplay(line: Int): Option[InfoEntry] = line match {
    case 0 => InfoEntryKind.CycleLength.value(Text.translate("bdlib.format.amount.unit", DecFormat.dec2(1 / workSpeed / 20), Text.unit("seconds")))
    case 1 => InfoEntryKind.EnergyUsed.value(Text.energyPerTick(powerUse))
    case 2 if parallelProcess > 1 => InfoEntryKind.ItemsPerCycle.value(Text.string(DecFormat.round(parallelProcess)))
    case _ => None
  }

  override def getDisplayName: Component = Text.translate(Blocks.mixer.block.get().getDescriptionId)
  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new MixerContainer(this, playerInventory, id)

  //noinspection ComparingUnrelatedTypes
  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == Capabilities.CAP_ITEM_HANDLER)
      inventoryHandler.cast()
    else if (cap == Capabilities.CAP_ENERGY_HANDLER)
      powerHandler.cast()
    else if (cap == Capabilities.CAP_FLUID_HANDLER)
      fluidHandler.cast()
    else
      super.getCapability(cap, side)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    powerHandler.invalidate()
    inventoryHandler.invalidate()
    fluidHandler.invalidate()
  }
}
