package net.bdew.advtech.worldgen

import net.bdew.advtech.worldgen.features.{CountPlacementConfigType, HeightRangePlacementConfigType, OreFeatureNormal}
import net.bdew.advtech.worldgen.retro.RetrogenTracker
import net.bdew.advtech.{AdvTech, Config}
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.GenerationStep.Decoration
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.BiomeLoadingEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.ForgeRegistries
import org.apache.logging.log4j.{LogManager, Logger}

object WorldGeneration {
  var features = List.empty[WorldgenPlaced]
  val log: Logger = LogManager.getLogger

  val FEATURE_ORE_NORMAL = new OreFeatureNormal()

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
      event.getGeneration.addFeature(Decoration.UNDERGROUND_ORES, entry.feature)
  }

  def init(): Unit = {
    MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, onBiomeLoadingEvent)
    FMLJavaModLoadingContext.get.getModEventBus.addListener(onCommonSetup)

    ForgeRegistries.FEATURES.register(FEATURE_ORE_NORMAL.setRegistryName(new ResourceLocation(AdvTech.ModId, "ore_normal")))
    Registry.register(Registry.PLACEMENT_MODIFIERS, new ResourceLocation(AdvTech.ModId, "count_config"), CountPlacementConfigType)
    Registry.register(Registry.PLACEMENT_MODIFIERS, new ResourceLocation(AdvTech.ModId, "height_range_config"), HeightRangePlacementConfigType)

    RetrogenTracker.init()
  }
}
