package net.bdew.factorium.gui

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Color, Point, Rect}
import net.minecraft.network.chat.Component

class WidgetTextLines(textLines: => List[Component], x: Float, y: Float, color: Color) extends Widget {
  val rect = new Rect(x, y, 0, 0)
  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit = {
    for ((text, n) <- textLines.zipWithIndex)
      parent.drawText(m, text, Point(rect.x, rect.y + parent.getFontRenderer.lineHeight * n), color, false)
  }
}
