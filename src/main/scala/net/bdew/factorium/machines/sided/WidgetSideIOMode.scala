package net.bdew.factorium.machines.sided

import net.bdew.factorium.machines.processing.ProcessingTextures
import net.bdew.factorium.misc.AutoIOMode
import net.bdew.factorium.network.NetworkHandler
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Point, Rect}
import net.bdew.lib.{Client, Misc, Text}
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents

import java.util.Locale
import scala.collection.mutable

class WidgetSideIOMode(p: Point, ds: DataSlotSidedIOConfig, side: BlockSide.Value, facing: => Direction, pktConstructor: (BlockSide.Value, AutoIOMode.Value) => NetworkHandler.Message) extends Widget {
  val rect = new Rect(p, 16, 16)
  val iconRect = new Rect(p + (1, 1), 14, 14)

  private val values: List[AutoIOMode.Value] = AutoIOMode.values.toList.sortBy(_.id)

  override def drawBackground(graphics: GuiGraphics, mouse: Point): Unit = {
    if (rect.contains(mouse))
      parent.drawTexture(graphics, rect, ProcessingTextures.buttonHover)
    else
      parent.drawTexture(graphics, rect, ProcessingTextures.buttonBase)
  }

  override def draw(graphics: GuiGraphics, mouse: Point, partial: Float): Unit = {
    parent.drawTexture(graphics, iconRect, SidesTextures.ioMode(ds.get(side)))
  }

  override def handleTooltip(p: Point, tip: mutable.ArrayBuffer[Component]): Unit = {
    val dir = BlockSide.toDirection(facing, side)
    if (dir.getAxis == Direction.Axis.Y)
      tip += Text.translate(s"factorium.side.${side.toString.toLowerCase(Locale.US)}")
    else
      tip += Text.translate(s"factorium.gui.side.detail",
        Text.translate(s"factorium.side.${side.toString.toLowerCase(Locale.US)}"),
        Text.translate(s"bdlib.multiblock.face.${dir.toString.toLowerCase(Locale.US)}")
      )
    tip += Text.translate(s"factorium.iomode.${ds.get(side).toString.toLowerCase(Locale.US)}")
  }

  override def mouseClicked(p: Point, button: Int): Boolean = {
    Client.soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F))
    val nextMode: AutoIOMode.Value =
      if (button == 0)
        Misc.nextInSeq(values, ds.get(side))
      else
        Misc.prevInSeq(values, ds.get(side))
    ds.set(side, nextMode)
    NetworkHandler.sendToServer(pktConstructor(side, nextMode))
    true
  }
}
