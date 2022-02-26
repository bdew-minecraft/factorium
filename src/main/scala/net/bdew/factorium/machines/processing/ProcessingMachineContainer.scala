package net.bdew.factorium.machines.processing

import net.bdew.factorium.machines.sided.SidedItemIOContainer
import net.bdew.factorium.machines.upgradable.UpgradeableContainer
import net.bdew.factorium.network.RsModeConfigurableContainer
import net.bdew.lib.container.BaseContainer
import net.bdew.lib.container.switchable.{SwitchableContainer, SwitchableSlot}
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.misc.RSMode
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType

class ProcessingMachineContainer(val te: ProcessingMachineEntity, playerInventory: Inventory, id: Int, menuType: MenuType[_ <: ProcessingMachineContainer])
  extends BaseContainer(te.externalInventory, menuType, id)
    with ContainerDataSlots with RsModeConfigurableContainer with SidedItemIOContainer with UpgradeableContainer {

  override lazy val dataSource: ProcessingMachineEntity = te

  def initSlots(): Unit = {
    val showMainSlots: () => Boolean = () => getActiveMode == SwitchableContainer.NormalMode

    for (y <- 0 to 2; x <- 0 to 1) {
      this.addSlot(new SwitchableSlot(
        te.externalInventory,
        te.Slots.input(x + y * 2),
        35 + x * 18, 18 + y * 18,
        showMainSlots
      ))
    }

    for (y <- 0 to 2; x <- 0 to 1) {
      this.addSlot(new SwitchableSlot(
        te.externalInventory,
        te.Slots.output(x + y * 2),
        106 + x * 18, 18 + y * 18,
        showMainSlots
      ))
    }
  }

  initSlots()
  initUpgradeSlots(te.upgrades)
  bindPlayerInventory(playerInventory, 8, 84, 142)

  override def setRsMode(mode: RSMode.Value): Unit = {
    te.rsMode := mode
  }
}
