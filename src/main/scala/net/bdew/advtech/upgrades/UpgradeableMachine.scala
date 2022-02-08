package net.bdew.advtech.upgrades

import net.bdew.advtech.machines.BaseMachineEntity
import net.bdew.advtech.upgrades.gui.UpgradesContainer
import net.bdew.lib.Text
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.{Container, MenuProvider}

trait UpgradeableMachine extends BaseMachineEntity {
  def upgradesInventory: Container

  val upgradesMenuProvider: MenuProvider = new MenuProvider {
    override def getDisplayName: Component = Text.translate("advtech.gui.upgrades")
    override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
      new UpgradesContainer(UpgradeableMachine.this, playerInventory, id)
  }

  def statsDisplay: List[Component]
}
