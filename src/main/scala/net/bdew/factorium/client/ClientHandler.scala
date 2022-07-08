package net.bdew.factorium.client

import net.bdew.factorium.Factorium
import net.bdew.factorium.registries.Blocks
import net.minecraft.client.renderer.{ItemBlockRenderTypes, RenderType}
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

import java.util.function.Predicate

@Mod.EventBusSubscriber(value = Array(Dist.CLIENT), modid = Factorium.ModId, bus = Bus.MOD)
object ClientHandler {
  @SubscribeEvent
  def clientSetup(ev: FMLClientSetupEvent): Unit = {
    ev.enqueueWork(new Runnable {
      override def run(): Unit = {
        val renderTypeGlass: Predicate[RenderType] = (x: RenderType) => x == RenderType.cutout() || x == RenderType.translucent()
        ItemBlockRenderTypes.setRenderLayer(Blocks.crystalGlass.block.get(), renderTypeGlass)
        ItemBlockRenderTypes.setRenderLayer(Blocks.reinforcedGlass.block.get(), renderTypeGlass)
        ItemBlockRenderTypes.setRenderLayer(Blocks.darkGlass.block.get(), renderTypeGlass)
        ItemBlockRenderTypes.setRenderLayer(Blocks.glowGlass.block.get(), renderTypeGlass)
      }
    })
  }

  @SubscribeEvent
  def registerLoaders(ev: RegisterGeometryLoaders): Unit = {
    ev.register("connected_model", CustomModels.connectedModelLoader)
  }

  @SubscribeEvent
  def registerEntityRenderers(ev: EntityRenderersEvent.RegisterRenderers): Unit = {
    ev.registerBlockEntityRenderer(Blocks.pump.teType.get(), new PumpEntityRenderer(_))
  }
}
