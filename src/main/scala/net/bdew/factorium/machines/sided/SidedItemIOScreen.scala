package net.bdew.factorium.machines.sided

import net.bdew.factorium.network.MsgSetItemSidedIO
import net.bdew.lib.Text
import net.bdew.lib.container.switchable.SwitchableScreen
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets.WidgetLabel

trait SidedItemIOScreen[T <: SidedItemIOContainer] extends SwitchableScreen[T] {
  override def initSwitchable(): Unit = {
    super.initSwitchable()

    addMode(SidedItemIOContainer.SidedItemIOMode, SidesTextures.screen) { sub =>
      sub.add(new WidgetLabel(Text.translate("factorium.gui.sidecfg"), 8, 6, Color.darkGray))

      sub.add(new WidgetSideIOMode(Point(80, 36), getMenu.te.itemIOConfig, BlockSide.FRONT, getMenu.te.getFacing, MsgSetItemSidedIO))
      sub.add(new WidgetSideIOMode(Point(80, 18), getMenu.te.itemIOConfig, BlockSide.TOP, getMenu.te.getFacing, MsgSetItemSidedIO))
      sub.add(new WidgetSideIOMode(Point(80, 54), getMenu.te.itemIOConfig, BlockSide.BOTTOM, getMenu.te.getFacing, MsgSetItemSidedIO))
      sub.add(new WidgetSideIOMode(Point(62, 36), getMenu.te.itemIOConfig, BlockSide.LEFT, getMenu.te.getFacing, MsgSetItemSidedIO))
      sub.add(new WidgetSideIOMode(Point(98, 36), getMenu.te.itemIOConfig, BlockSide.RIGHT, getMenu.te.getFacing, MsgSetItemSidedIO))
      sub.add(new WidgetSideIOMode(Point(62, 54), getMenu.te.itemIOConfig, BlockSide.BACK, getMenu.te.getFacing, MsgSetItemSidedIO))
    }
  }
}
