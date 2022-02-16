package net.bdew.advtech.machines.processing

import net.bdew.advtech.gui.WidgetMode
import net.bdew.advtech.machines.MachineTextures
import net.bdew.advtech.machines.sided.{SidedItemIOContainer, SidedItemIOScreen}
import net.bdew.advtech.machines.upgradable.{UpgradableScreen, UpgradeableContainer}
import net.bdew.advtech.network.MsgSetRsMode
import net.bdew.lib.Text
import net.bdew.lib.container.switchable.{SwitchableContainer, SwitchableScreen}
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.{WidgetButtonIcon, WidgetLabel, WidgetProgressBar}
import net.bdew.lib.power.WidgetPowerGauge
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class ProcessingMachineScreen(container: ProcessingMachineContainer, playerInv: Inventory) extends BaseScreen(container, playerInv, container.te.getDisplayName) with SwitchableScreen[ProcessingMachineContainer] with UpgradableScreen[ProcessingMachineContainer] with SidedItemIOScreen[ProcessingMachineContainer] {
  override def init(): Unit = {
    initGui(176, 166)
    initSwitchable()

    widgets.add(new WidgetLabel(playerInv.getName, 8, rect.h - 93, Color.darkGray))

    addMode(SwitchableContainer.NormalMode, MachineTextures.screen) { sub =>
      sub.add(new WidgetLabel(title, 8, 6, Color.darkGray))

      sub.add(new WidgetPowerGauge(Rect(10, 18, 12, 52), MachineTextures.powerFill, container.te.power))

      sub.add(new WidgetProgressBar(Rect(75, 35, 24, 16), MachineTextures.arrow, container.te.progress))

      sub.add(new WidgetButtonIcon(Point(152, 18), openUpgrades, MachineTextures.buttonBase, MachineTextures.buttonHover) {
        override def icon: Texture = MachineTextures.upgrades
        override def hover: Component = Text.translate("advtech.gui.upgrades")
      })

      sub.add(WidgetMode(Point(152, 36), container.te.rsMode, MachineTextures.rsMode, MsgSetRsMode, "bdlib.rsmode"))

      sub.add(new WidgetButtonIcon(Point(152, 54), openSides, MachineTextures.buttonBase, MachineTextures.buttonHover) {
        override def icon: Texture = MachineTextures.ioConfig
        override def hover: Component = Text.translate("advtech.gui.sidecfg")
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
