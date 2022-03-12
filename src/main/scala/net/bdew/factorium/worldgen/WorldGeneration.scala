package net.bdew.factorium.worldgen

import net.bdew.factorium.Config
import net.bdew.factorium.worldgen.retro.RetrogenTracker
import net.minecraft.world.level.levelgen.GenerationStep.Decoration
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.BiomeLoadingEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.{LogManager, Logger}

object WorldGeneration {
  var features = List.empty[WorldgenPlaced]
  val log: Logger = LogManager.getLogger

  def registerFeatures(): Unit = {
    features = Config.WorldGen.configs.map(_.createPlaced())
    log.info(s"Registered ${features.size} worldgen features")
  }

  def onCommonSetup(ev: FMLCommonSetupEvent): Unit = {
    ev.enqueueWork((() => {
      try {
        registerFeatures()
      } catch {
        case e: Throwable => log.error("Error in worldgen setup", e)
      }
    }): Runnable)
  }

  def onBiomeLoadingEvent(event: BiomeLoadingEvent): Unit = {
    for (entry <- features if entry.filter.matches(event.getCategory))
      event.getGeneration.addFeature(Decoration.UNDERGROUND_ORES, entry.featureHolder)
  }

  def init(): Unit = {
    MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, onBiomeLoadingEvent)
    FMLJavaModLoadingContext.get.getModEventBus.addListener(onCommonSetup)
    PlacementModifiers.init()
    Features.init()
    RetrogenTracker.init()
  }
}
