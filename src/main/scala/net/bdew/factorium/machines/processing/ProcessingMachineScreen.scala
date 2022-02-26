package net.bdew.factorium.machines.processing

import net.bdew.factorium.gui.WidgetMode
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

class ProcessingMachineScreen(container: ProcessingMachineContainer, playerInv: Inventory) extends BaseScreen[ProcessingMachineContainer](container, playerInv, container.te.getDisplayName) with SwitchableScreen[ProcessingMachineContainer] with UpgradableScreen[ProcessingMachineContainer] with SidedItemIOScreen[ProcessingMachineContainer] {
  override def init(): Unit = {
    initGui(176, 166)
    initSwitchable()

    widgets.add(new WidgetLabel(playerInv.getName, 8, rect.h - 93, Color.darkGray))

    addMode(SwitchableContainer.NormalMode, ProcessingTextures.screen) { sub =>
      sub.add(new WidgetLabel(title, 8, 6, Color.darkGray))

      sub.add(new WidgetPowerGauge(Rect(10, 18, 12, 52), ProcessingTextures.powerFill, container.te.power))

      sub.add(new WidgetProgressBar(Rect(75, 35, 24, 16), ProcessingTextures.arrow, container.te.progress))

      sub.add(new WidgetButtonIcon(Point(152, 18), openUpgrades, ProcessingTextures.buttonBase, ProcessingTextures.buttonHover) {
        override def icon: Texture = ProcessingTextures.upgrades
        override def hover: Component = Text.translate("factorium.gui.upgrades")
      })

      sub.add(WidgetMode(Point(152, 36), container.te.rsMode, ProcessingTextures.rsMode, MsgSetRsMode, "bdlib.rsmode"))

      sub.add(new WidgetButtonIcon(Point(152, 54), openSides, ProcessingTextures.buttonBase, ProcessingTextures.buttonHover) {
        override def icon: Texture = ProcessingTextures.ioConfig
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
