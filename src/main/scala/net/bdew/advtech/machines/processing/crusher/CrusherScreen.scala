package net.bdew.advtech.machines.processing.crusher

import net.bdew.advtech.gui.WidgetMode
import net.bdew.advtech.machines.MachineTextures
import net.bdew.advtech.network.{MsgOpenUpgrades, MsgSetAutoIOMode, MsgSetRsMode, NetworkHandler}
import net.bdew.lib.Text
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetLabel, WidgetProgressBar}
import net.bdew.lib.power.WidgetPowerGauge
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class CrusherScreen(container: CrusherContainer, playerInv: Inventory) extends BaseScreen(container, playerInv, container.te.getDisplayName) {

  override val background: Texture = MachineTextures.screen

  override def init(): Unit = {
    initGui(176, 166)

    widgets.add(new WidgetLabel(title, 8, 6, Color.darkGray))
    widgets.add(new WidgetLabel(playerInv.getName, 8, rect.h - 93, Color.darkGray))

    widgets.add(new WidgetPowerGauge(Rect(10, 18, 12, 52), MachineTextures.powerFill, container.te.power))

    widgets.add(new WidgetProgressBar(Rect(75, 35, 24, 16), MachineTextures.arrow, container.te.progress))

    widgets.add(new WidgetButtonIcon(Point(152, 18), openUpgrades, MachineTextures.buttonBase, MachineTextures.buttonHover) {
      override def icon: Texture = MachineTextures.upgrades
      override def hover: Component = Text.translate("advtech.gui.openupgrades")
    })

    widgets.add(WidgetMode(Point(152, 36), container.te.rsMode, MachineTextures.rsMode, MsgSetRsMode, "bdlib.rsmode"))
    widgets.add(WidgetMode(Point(152, 54), container.te.ioMode, MachineTextures.autoIoMode, MsgSetAutoIOMode, "advtech.iomode"))
  }

  def openUpgrades(b: WidgetButtonIcon): Unit = {
    NetworkHandler.sendToServer(MsgOpenUpgrades())
  }
}
