package net.bdew.advtech.upgrades.gui

import net.bdew.advtech.registries.Containers
import net.bdew.advtech.upgrades.UpgradeableMachine
import net.bdew.lib.container.{BaseContainer, SlotValidating}
import net.bdew.lib.data.base.ContainerDataSlots
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraftforge.network.NetworkHooks

class UpgradesContainer(val te: UpgradeableMachine, playerInventory: Inventory, id: Int)
  extends BaseContainer(te.upgradesInventory, Containers.upgrades.get(), id) with ContainerDataSlots {

  override lazy val dataSource: UpgradeableMachine = te

  for (y <- 0 to 1; x <- 0 to 2) {
    this.addSlot(new SlotValidating(
      te.upgradesInventory,
      x + y * 3,
      15 + x * 18, 25 + y * 18
    ))
  }

  bindPlayerInventory(playerInventory, 8, 84, 142)

  def reopenMachine(player: ServerPlayer): Unit = {
    NetworkHooks.openGui(player, te, te.getBlockPos)
  }
}
