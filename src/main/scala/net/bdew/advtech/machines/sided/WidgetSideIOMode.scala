package net.bdew.advtech.machines.sided

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.advtech.machines.processing.ProcessingTextures
import net.bdew.advtech.misc.AutoIOMode
import net.bdew.advtech.network.NetworkHandler
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Point, Rect}
import net.bdew.lib.{Client, Misc, Text}
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

  override def drawBackground(m: PoseStack, mouse: Point): Unit = {
    if (rect.contains(mouse))
      parent.drawTexture(m, rect, ProcessingTextures.buttonHover)
    else
      parent.drawTexture(m, rect, ProcessingTextures.buttonBase)
  }

  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit = {
    parent.drawTexture(m, iconRect, SidesTextures.ioMode(ds.get(side)))
  }

  override def handleTooltip(p: Point, tip: mutable.ArrayBuffer[Component]): Unit = {
    val dir = BlockSide.toDirection(facing, side)
    if (dir.getAxis == Direction.Axis.Y)
      tip += Text.translate(s"advtech.side.${side.toString.toLowerCase(Locale.US)}")
    else
      tip += Text.translate(s"advtech.gui.side.detail",
        Text.translate(s"advtech.side.${side.toString.toLowerCase(Locale.US)}"),
        Text.translate(s"bdlib.multiblock.face.${dir.toString.toLowerCase(Locale.US)}")
      )
    tip += Text.translate(s"advtech.iomode.${ds.get(side).toString.toLowerCase(Locale.US)}")
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
