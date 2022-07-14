package net.bdew.factorium.client

import net.bdew.factorium.Factorium
import net.bdew.factorium.registries.Blocks
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus

@Mod.EventBusSubscriber(value = Array(Dist.CLIENT), modid = Factorium.ModId, bus = Bus.MOD)
object ClientHandler {
  @SubscribeEvent
  def registerLoaders(ev: RegisterGeometryLoaders): Unit = {
    ev.register("connected_model", CustomModels.connectedModelLoader)
  }

  @SubscribeEvent
  def registerEntityRenderers(ev: EntityRenderersEvent.RegisterRenderers): Unit = {
    ev.registerBlockEntityRenderer(Blocks.pump.teType.get(), new PumpEntityRenderer(_))
  }
}
