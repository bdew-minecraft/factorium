package net.bdew.advtech.machines.crusher

import net.bdew.advtech.Config
import net.bdew.advtech.machines.{BaseMachineEntity, MachineRecipes}
import net.bdew.advtech.misc.AutoIOMode
import net.bdew.advtech.registries.Blocks
import net.bdew.advtech.upgrades.{DataSlotUpgrades, UpgradeableMachine}
import net.bdew.lib.Text
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.handlers.{PowerEnergyHandler, SidedInventoryItemHandler}
import net.bdew.lib.data.base.UpdateKind
import net.bdew.lib.data.{DataSlotEnum, DataSlotFloat, DataSlotInventory}
import net.bdew.lib.inventory.RestrictedInventory
import net.bdew.lib.misc.RSMode
import net.bdew.lib.power.DataSlotPower
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

import scala.util.Random


class CrusherEntity(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends BaseMachineEntity(teType, pos, state) with UpgradeableMachine {
  object Slots {
    val input: Range = 0 to 5
    val output: Range = 6 to 11
  }

  def config: CrusherConfig = Config.Machines.Crusher

  val inventory: DataSlotInventory = DataSlotInventory("inv", this, 18)
  val upgradesInventory: DataSlotInventory = new DataSlotUpgrades(this)
  val power: DataSlotPower = DataSlotPower("power", this)
  val rsMode: DataSlotEnum[RSMode.type] = DataSlotEnum("rsMode", this, RSMode)
  val ioMode: DataSlotEnum[AutoIOMode.type] = DataSlotEnum("ioMode", this, AutoIOMode)

  val progress: DataSlotFloat = DataSlotFloat("progress", this, 0.72f).setUpdate(UpdateKind.GUI, UpdateKind.SAVE)

  val externalInventory = new RestrictedInventory(inventory,
    canExtract = (slot, _) => Slots.output.contains(slot),
    canInsert = (slot, stack, _) => Slots.input.contains(slot) && isValidInput(stack)
  )

  val inventoryHandler: LazyOptional[IItemHandler] = SidedInventoryItemHandler.create(externalInventory)
  val powerHandler: LazyOptional[IEnergyStorage] = PowerEnergyHandler.create(power, true, false)

  updatePowerHandler()

  serverTick.listen(doTick)

  def doTick(): Unit = {
  }

  def updatePowerHandler(): Unit = {
    power.maxReceive = config.baseChargingRate()
    power.capacity = config.basePowerCapacity()
  }

  def isValidInput(stack: ItemStack): Boolean = MachineRecipes.crusher.exists(_.input.test(stack))

  override def statsDisplay: List[Component] =
    List(Text.string("TEST 1"), Text.string("TEST 2"), Text.string(s"TEST 3 ${Random.nextInt(100)}"), Text.string("4"), Text.string("5"))

  override def getDisplayName: Component = Text.translate(Blocks.crusher.block.get().getDescriptionId)
  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new CrusherContainer(this, playerInventory, id)

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
