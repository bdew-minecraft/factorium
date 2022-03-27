package net.bdew.factorium.gui

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.factorium.network.{MsgClearBuffers, NetworkHandler}
import net.bdew.lib.data.DataSlotTankBase
import net.bdew.lib.gui.widgets.{WidgetFluidGauge, WidgetSubContainer}
import net.bdew.lib.gui.{Point, Rect, Texture}
import net.bdew.lib.{Client, Text}
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents

import scala.collection.mutable.ArrayBuffer

class WidgetClearableTank(rect: Rect, overlay: Texture, button: Texture, dSlot: DataSlotTankBase, clearId: Int) extends WidgetSubContainer(rect) {
  private val buttonRect = Rect(rect.x + rect.w - 5, rect.y, 5, 5)

  add(new WidgetFluidGauge(Rect(0, 2, rect.w - 2, rect.h - 2), overlay, dSlot))

  def buttonVisible: Boolean = !dSlot.isEmpty

  override def draw(matrix: PoseStack, mouse: Point, partial: Float): Unit = {
    super.draw(matrix, mouse, partial)
    if (buttonVisible && rect.contains(mouse)) {
      parent.drawTexture(matrix, buttonRect, button)
    }
  }

  override def handleTooltip(mouse: Point, tip: ArrayBuffer[Component]): Unit = {
    if (buttonVisible && buttonRect.contains(mouse)) {
      tip += Text.translate("factorium.gui.clear.tank")
    } else super.handleTooltip(mouse, tip)
  }

  override def mouseClicked(mouse: Point, bt: Int): Boolean = {
    if (buttonVisible && buttonRect.contains(mouse + rect.origin)) {
      Client.soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F))
      NetworkHandler.sendToServer(MsgClearBuffers(clearId))
      true
    } else super.mouseClicked(mouse, bt)
  }
}