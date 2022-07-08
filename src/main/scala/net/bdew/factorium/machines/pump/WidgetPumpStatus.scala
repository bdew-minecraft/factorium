package net.bdew.factorium.machines.pump

import com.mojang.blaze3d.vertex.PoseStack
import net.bdew.lib.Text
import net.bdew.lib.gui.widgets.Widget
import net.bdew.lib.gui.{Color, Point, Rect, Texture}
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
import net.minecraftforge.fluids.FluidStack

import scala.collection.mutable.ArrayBuffer

class WidgetPumpStatus(val rect: Rect, pump: PumpEntity) extends Widget {
  override def handleTooltip(p: Point, tip: ArrayBuffer[Component]): Unit = {
    pump.pumpState.get match {
      case PumpState.Lowering(d) =>
        tip += Text.translate("factorium.gui.pump.scanning")
        tip += Text.translate("factorium.gui.pump.at", (pump.getBlockPos.getY - d).toString)
      case PumpState.Pumping(d, inf, fluid) =>
        tip += Text.translate("factorium.gui.pump.working", new FluidStack(fluid, 1000).getDisplayName)
        if (inf)
          tip += Text.translate("factorium.gui.pump.infinite").withStyle(ChatFormatting.YELLOW)
        tip += Text.translate("factorium.gui.pump.at", (pump.getBlockPos.getY - d).toString)
      case PumpState.Scanning(d, _) =>
        tip += Text.translate("factorium.gui.pump.scanning")
        tip += Text.translate("factorium.gui.pump.at", (pump.getBlockPos.getY - d).toString)
      case PumpState.Invalid =>
        tip += Text.translate("factorium.gui.pump.invalid")
    }
  }

  override def draw(m: PoseStack, mouse: Point, partial: Float): Unit = {
    pump.pumpState.get match {
      case PumpState.Lowering(_) =>
        parent.drawTexture(m, rect, PumpTextures.scanning)
      case PumpState.Scanning(_, _) =>
        parent.drawTexture(m, rect, PumpTextures.scanning)
      case PumpState.Pumping(_, _, fluid) =>
        val props = IClientFluidTypeExtensions.of(fluid)
        val color = Color.fromInt(props.getTintColor)
        val icon = Texture.block(props.getStillTexture)
        parent.drawTexture(m, rect, icon, color)
      case PumpState.Invalid =>
        parent.drawTexture(m, rect, PumpTextures.clear)
    }
  }
}
