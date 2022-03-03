package net.bdew.factorium.machines.extruder

import net.bdew.factorium.machines.sided.SidedItemIOContainer
import net.bdew.factorium.machines.upgradable.UpgradeableContainer
import net.bdew.factorium.network.RsModeConfigurableContainer
import net.bdew.factorium.registries.Containers
import net.bdew.lib.container.BaseContainer
import net.bdew.lib.container.switchable.{SwitchableContainer, SwitchableSlot}
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.misc.RSMode
import net.minecraft.world.entity.player.Inventory

class ExtruderContainer(val te: ExtruderEntity, playerInventory: Inventory, id: Int)
  extends BaseContainer(te.externalInventory, Containers.extruder.get(), id)
    with ContainerDataSlots with RsModeConfigurableContainer with SidedItemIOContainer with UpgradeableContainer {

  override lazy val dataSource: ExtruderEntity = te

  def initSlots(): Unit = {
    val showMainSlots: () => Boolean = () => getActiveMode == SwitchableContainer.NormalMode

    this.addSlot(new SwitchableSlot(
      te.externalInventory,
      te.Slots.die,
      44, 18,
      showMainSlots
    ))

    this.addSlot(new SwitchableSlot(
      te.externalInventory,
      te.Slots.input,
      44, 54,
      showMainSlots
    ))

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
