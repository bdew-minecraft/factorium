package net.bdew.advtech.upgrades.gui

import net.bdew.advtech.network.{MsgCloseUpgrades, NetworkHandler}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetLabel
import net.minecraft.world.entity.player.Inventory

class UpgradesScreen(container: UpgradesContainer, playerInv: Inventory) extends BaseScreen(container, playerInv, container.te.upgradesMenuProvider.getDisplayName) {

  override val background: Texture = UpgradesTextures.screen

  override def init(): Unit = {
    initGui(176, 166)

    widgets.add(new WidgetLabel(title, 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(playerInv.getName, 8, rect.h - 93, Color.darkGray))

    for (i <- 0 until 5)
      widgets.add(new WidgetInfoDisplay(Rect(82, 18 + 10 * i, 84, 9), container.te, i))
  }

  override def onClose(): Unit = {
    NetworkHandler.sendToServer(MsgCloseUpgrades())
  }
}
