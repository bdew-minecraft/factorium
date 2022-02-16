package net.bdew.advtech.machines.upgradable

import net.bdew.lib.Text
import net.bdew.lib.container.switchable.SwitchableScreen
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetLabel

trait UpgradableScreen[T <: UpgradeableContainer] extends SwitchableScreen[T] {
  override def initSwitchable(): Unit = {
    super.initSwitchable()

    addMode(UpgradeableContainer.UpgradesMode, UpgradesTextures.screen) { sub =>
      sub.add(new WidgetLabel(Text.translate("advtech.gui.upgrades"), 8, 6, Color.darkGray))
      for (i <- 0 until 5)
        sub.add(new WidgetInfoDisplay(Rect(82, 18 + 10 * i, 84, 9), getMenu.te, i))
    }
  }
}
