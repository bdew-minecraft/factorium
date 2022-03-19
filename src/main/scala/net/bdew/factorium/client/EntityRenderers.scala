package net.bdew.factorium.client

import net.bdew.factorium.Factorium
import net.bdew.factorium.registries.Blocks
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus

@Mod.EventBusSubscriber(modid = Factorium.ModId, value = Array(Dist.CLIENT), bus = Bus.MOD)
object EntityRenderers {
  @SubscribeEvent
  def register(ev: EntityRenderersEvent.RegisterRenderers): Unit = {
    ev.registerBlockEntityRenderer(Blocks.pump.teType.get(), new PumpEntityRenderer(_))
  }
}

