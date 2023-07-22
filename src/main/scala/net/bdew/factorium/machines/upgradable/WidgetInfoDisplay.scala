package net.bdew.factorium.machines.upgradable

import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Color, Point, Rect}
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

import scala.collection.mutable.ArrayBuffer

class WidgetInfoDisplay(val rect: Rect, src: InfoSource, line: Int) extends Widget {
  final val iconRect = Rect(1.5, 1.5, 6, 6)

  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit = {
    src.statsDisplay(line).foreach({ info =>
      if (iconRect.contains(p - rect.origin)) tip += info.kind.name
    })
  }

  override def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit = {
    src.statsDisplay(line).foreach({ info =>
      parent.drawTexture(graphics, iconRect + rect.origin, UpgradesTextures.infoIcons(info.kind))
      parent.drawText(graphics, info.text, rect.origin + (12, 1), Color.white, false)
    })
  }
}
