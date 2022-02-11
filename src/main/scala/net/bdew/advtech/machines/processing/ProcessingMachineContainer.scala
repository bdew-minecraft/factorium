package net.bdew.advtech.machines.processing

import net.bdew.advtech.misc.AutoIOMode
import net.bdew.advtech.network.{AutoIOConfigurableContainer, RsModeConfigurableContainer}
import net.bdew.advtech.upgrades.UpgradeableContainer
import net.bdew.lib.container.{BaseContainer, SlotValidating}
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.misc.RSMode
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.network.NetworkHooks

class ProcessingMachineContainer(val te: ProcessingMachineEntity, playerInventory: Inventory, id: Int, menuType: MenuType[_ <: ProcessingMachineContainer])
  extends BaseContainer(te.externalInventory, menuType, id)
    with ContainerDataSlots with RsModeConfigurableContainer with AutoIOConfigurableContainer with UpgradeableContainer {

  override lazy val dataSource: ProcessingMachineEntity = te

  for (y <- 0 to 2; x <- 0 to 1) {
    this.addSlot(new SlotValidating(
      te.externalInventory,
      te.Slots.input(x + y * 2),
      35 + x * 18, 18 + y * 18
    ))
  }

  for (y <- 0 to 2; x <- 0 to 1) {
    this.addSlot(new SlotValidating(
      te.externalInventory,
      te.Slots.output(x + y * 2),
      106 + x * 18, 18 + y * 18
    ))
  }

  bindPlayerInventory(playerInventory, 8, 84, 142)

  override def setRsMode(mode: RSMode.Value): Unit = {
    te.rsMode := mode
  }

  override def setAutoIoMode(mode: AutoIOMode.Value): Unit = {
    te.ioMode := mode
  }

  override def openUpgrades(player: ServerPlayer): Unit = {
    NetworkHooks.openGui(player, te.upgradesMenuProvider, te.getBlockPos)
  }
}
