package net.bdew.factorium.client

import com.mojang.blaze3d.vertex.{PoseStack, VertexConsumer}
import net.bdew.factorium.machines.pump.PumpEntity
import net.minecraft.client.renderer.blockentity.{BlockEntityRenderer, BlockEntityRendererProvider}
import net.minecraft.client.renderer.{MultiBufferSource, RenderType}
import org.joml.{Matrix3f, Matrix4f}

class PumpEntityRenderer(ctx: BlockEntityRendererProvider.Context) extends BlockEntityRenderer[PumpEntity] {
  override def render(entity: PumpEntity, partial: Float, stack: PoseStack, buffers: MultiBufferSource, light: Int, overlay: Int): Unit = {
    val hl = entity.hoseLength
    if (hl <= 0) return

    val buffer = buffers.getBuffer(RenderType.solid())
    stack.pushPose()
    stack.translate(0.5, 0, 0.5)

    val pose = stack.last().pose()
    val normal = stack.last().normal()

    val texture = MiscTextures.pumpHose.texture

    val u1 = texture.u1
    val u2 = texture.u2
    val v1 = texture.v1
    val v2 = texture.v2
    val pv1 = v1 + (v2 - v1) * 1f / 8f
    val pv2 = v1 + (v2 - v1) * 7f / 8f

    for (y <- 0 until hl) {
      addSection(buffer, pose, normal, -y, -y + 1f, 0.15f, u1, u2, pv1, pv2, light, overlay)
    }

    addSection(buffer, pose, normal, -hl + 0.5f, -hl + 1, 0.15f, u1, u2, v1, pv2, light, overlay)

    addVertex(buffer, pose, normal, -0.15f, -hl + 0.5f, -0.15f, u1, v2, light, overlay)
    addVertex(buffer, pose, normal, 0.15f, -hl + 0.5f, -0.15f, u1, v1, light, overlay)
    addVertex(buffer, pose, normal, 0.15f, -hl + 0.5f, 0.15f, u2, v1, light, overlay)
    addVertex(buffer, pose, normal, -0.15f, -hl + 0.5f, 0.15f, u2, v2, light, overlay)

    stack.popPose()
  }

  def addSection(buffer: VertexConsumer, pose: Matrix4f, normal: Matrix3f, y1: Float, y2: Float, t: Float, u1: Float, u2: Float, v1: Float, v2: Float, light: Int, overlay: Int): Unit = {
    addVertex(buffer, pose, normal, -t, y2, +t, u1, v2, light, overlay)
    addVertex(buffer, pose, normal, -t, y1, +t, u1, v1, light, overlay)
    addVertex(buffer, pose, normal, +t, y1, +t, u2, v1, light, overlay)
    addVertex(buffer, pose, normal, +t, y2, +t, u2, v2, light, overlay)

    addVertex(buffer, pose, normal, -t, y2, -t, u1, v2, light, overlay)
    addVertex(buffer, pose, normal, +t, y2, -t, u2, v2, light, overlay)
    addVertex(buffer, pose, normal, +t, y1, -t, u2, v1, light, overlay)
    addVertex(buffer, pose, normal, -t, y1, -t, u1, v1, light, overlay)

    addVertex(buffer, pose, normal, -t, y2, -t, u1, v2, light, overlay)
    addVertex(buffer, pose, normal, -t, y1, -t, u1, v1, light, overlay)
    addVertex(buffer, pose, normal, -t, y1, +t, u2, v1, light, overlay)
    addVertex(buffer, pose, normal, -t, y2, +t, u2, v2, light, overlay)

    addVertex(buffer, pose, normal, +t, y2, -t, u1, v2, light, overlay)
    addVertex(buffer, pose, normal, +t, y2, +t, u2, v2, light, overlay)
    addVertex(buffer, pose, normal, +t, y1, +t, u2, v1, light, overlay)
    addVertex(buffer, pose, normal, +t, y1, -t, u1, v1, light, overlay)

  }

  def addVertex(buffer: VertexConsumer, pose: Matrix4f, normal: Matrix3f, x: Float, y: Float, z: Float, u: Float, v: Float, light: Int, overlay: Int): Unit = {
    buffer.vertex(pose, x, y, z)
      .color(0xffffffff)
      .uv(u, v)
      .overlayCoords(overlay)
      .uv2(light)
      .normal(normal, 0, 1, 0)
      .endVertex()

  }

  override def shouldRenderOffScreen(ent: PumpEntity): Boolean = true
}
