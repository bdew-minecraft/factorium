package net.bdew.factorium.machines.pump

import net.bdew.factorium.gui.WidgetMode
import net.bdew.factorium.machines.upgradable.{UpgradableScreen, UpgradeableContainer}
import net.bdew.factorium.network.{MsgClearBuffers, MsgSetRsMode, NetworkHandler}
import net.bdew.lib.Text
import net.bdew.lib.container.switchable.{SwitchableContainer, SwitchableScreen}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetFluidGauge, WidgetLabel, WidgetProgressBar}
import net.bdew.lib.power.WidgetPowerGauge
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class PumpScreen(container: PumpContainer, playerInv: Inventory) extends BaseScreen(container, playerInv, container.te.getDisplayName) with SwitchableScreen[PumpContainer] with UpgradableScreen[PumpContainer] {
  override def init(): Unit = {
    initGui(176, 166)
    initSwitchable()

    widgets.add(new WidgetLabel(playerInv.getName, 8, rect.h - 93, Color.darkGray))

    addMode(SwitchableContainer.NormalMode, PumpTextures.screen) { sub =>
      sub.add(new WidgetLabel(title, 8, 6, Color.darkGray))

      sub.add(new WidgetPowerGauge(Rect(10, 18, 12, 52), PumpTextures.powerFill, container.te.power))

      sub.add(new WidgetProgressBar(Rect(60, 35, 55, 18), PumpTextures.arrow, container.te.progress))

      sub.add(new WidgetFluidGauge(Rect(125, 24, 16, 39), PumpTextures.tankOverlay, container.te.tank))

      sub.add(new WidgetPumpStatus(Rect(35, 36, 16, 16), container.te))

      sub.add(new WidgetButtonIcon(Point(152, 18), openUpgrades, PumpTextures.buttonBase, PumpTextures.buttonHover) {
        override def icon: Texture = PumpTextures.upgrades
        override def hover: Component = Text.translate("factorium.gui.upgrades")
      })

      sub.add(WidgetMode(Point(152, 36), container.te.rsMode, PumpTextures.rsMode, MsgSetRsMode, "bdlib.rsmode"))

      sub.add(new WidgetButtonIcon(Point(152, 54), clearBuffers, PumpTextures.buttonBase, PumpTextures.buttonRed) {
        override def icon: Texture = PumpTextures.clear
        override def hover: Component = Text.translate("factorium.gui.clear")
      })
    }
  }

  def clearBuffers(b: WidgetButtonIcon): Unit = {
    NetworkHandler.sendToServer(MsgClearBuffers(0))
  }

  def openUpgrades(b: WidgetButtonIcon): Unit = {
    container.activateModeClient(UpgradeableContainer.UpgradesMode)
  }
}
