package net.bdew.factorium.machines.mixer

import net.bdew.factorium.gui.{WidgetClearableTank, WidgetMode}
import net.bdew.factorium.machines.sided.{SidedItemIOContainer, SidedItemIOScreen}
import net.bdew.factorium.machines.upgradable.{UpgradableScreen, UpgradeableContainer}
import net.bdew.factorium.network.MsgSetRsMode
import net.bdew.lib.Text
import net.bdew.lib.container.switchable.{SwitchableContainer, SwitchableScreen}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetLabel, WidgetProgressBar}
import net.bdew.lib.power.WidgetPowerGauge
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class MixerScreen(container: MixerContainer, playerInv: Inventory) extends BaseScreen(container, playerInv, container.te.getDisplayName) with SwitchableScreen[MixerContainer] with UpgradableScreen[MixerContainer] with SidedItemIOScreen[MixerContainer] {
  override def init(): Unit = {
    initGui(176, 166)
    initSwitchable()

    widgets.add(new WidgetLabel(playerInv.getName, 8, rect.h - 93, Color.darkGray))

    addMode(SwitchableContainer.NormalMode, MixerTextures.screen) { sub =>
      sub.add(new WidgetLabel(title, 8, 6, Color.darkGray))

      sub.add(new WidgetPowerGauge(Rect(10, 18, 12, 52), MixerTextures.powerFill, container.te.power))

      sub.add(new WidgetProgressBar(Rect(75, 35, 24, 16), MixerTextures.arrow, container.te.progress))

      sub.add(new WidgetClearableTank(Rect(31, 22, 18, 41), MixerTextures.tankOverlay, MixerTextures.clearButton, container.te.inputTank, 0))
      sub.add(new WidgetClearableTank(Rect(128, 22, 18, 41), MixerTextures.tankOverlay, MixerTextures.clearButton, container.te.outputTank, 1))

      sub.add(new WidgetButtonIcon(Point(152, 18), openUpgrades, MixerTextures.buttonBase, MixerTextures.buttonHover) {
        override def icon: Texture = MixerTextures.upgrades
        override def hover: Component = Text.translate("factorium.gui.upgrades")
      })

      sub.add(WidgetMode(Point(152, 36), container.te.rsMode, MixerTextures.rsMode, MsgSetRsMode, "bdlib.rsmode"))

      sub.add(new WidgetButtonIcon(Point(152, 54), openSides, MixerTextures.buttonBase, MixerTextures.buttonHover) {
        override def icon: Texture = MixerTextures.ioConfig
        override def hover: Component = Text.translate("factorium.gui.sidecfg")
      })
    }
  }

  def openUpgrades(b: WidgetButtonIcon): Unit = {
    container.activateModeClient(UpgradeableContainer.UpgradesMode)
  }

  def openSides(b: WidgetButtonIcon): Unit = {
    container.activateModeClient(SidedItemIOContainer.SidedItemIOMode)
  }
}
