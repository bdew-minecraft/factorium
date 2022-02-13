package net.bdew.advtech.worldgen

import net.bdew.advtech.Config
import net.minecraft.world.level.levelgen.GenerationStep.Decoration
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.BiomeLoadingEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.{LogManager, Logger}

object OreGeneration {
  var features = List.empty[(BiomeCatFilter, PlacedFeature)]
  val log: Logger = LogManager.getLogger

  def registerFeatures(): Unit = {
    features = Config.OreGen.configs.map(ore => ore.oreGen.filter -> ore.createFeature)
    log.info(s"Registered ${features.size} oregen features")
  }

  def onCommonSetup(ev: FMLCommonSetupEvent): Unit = {
    ev.enqueueWork((() => {
      registerFeatures()
    }): Runnable)
  }

  def onBiomeLoadingEvent(event: BiomeLoadingEvent): Unit = {
    for ((filter, feature) <- features if filter.matches(event.getCategory))
      event.getGeneration.addFeature(Decoration.UNDERGROUND_ORES, feature)
  }

  def init(): Unit = {
    MinecraftForge.EVENT_BUS.addListener(onBiomeLoadingEvent)
    FMLJavaModLoadingContext.get.getModEventBus.addListener(onCommonSetup)
  }
}
