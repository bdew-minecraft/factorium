package net.bdew.advtech.machines.alloy

import net.bdew.advtech.machines.sided.SidedItemIOContainer
import net.bdew.advtech.machines.upgradable.UpgradeableContainer
import net.bdew.advtech.network.RsModeConfigurableContainer
import net.bdew.advtech.registries.Containers
import net.bdew.lib.container.BaseContainer
import net.bdew.lib.container.switchable.{SwitchableContainer, SwitchableSlot}
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.misc.RSMode
import net.minecraft.world.entity.player.Inventory

class AlloySmelterContainer(val te: AlloySmelterEntity, playerInventory: Inventory, id: Int)
  extends BaseContainer(te.externalInventory, Containers.alloySmelter.get(), id)
    with ContainerDataSlots with RsModeConfigurableContainer with SidedItemIOContainer with UpgradeableContainer {

  override lazy val dataSource: AlloySmelterEntity = te

  def initSlots(): Unit = {
    val showMainSlots: () => Boolean = () => getActiveMode == SwitchableContainer.NormalMode

    for (x <- 0 to 1) {
      this.addSlot(new SwitchableSlot(
        te.externalInventory,
        te.Slots.input(x),
        35 + x * 18, 36,
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
