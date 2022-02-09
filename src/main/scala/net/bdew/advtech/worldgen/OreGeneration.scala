package net.bdew.advtech.worldgen

import net.bdew.advtech.registries.{MetalEntry, MetalItemType, Metals}
import net.bdew.advtech.{AdvTech, Config}
import net.minecraft.data.BuiltinRegistries
import net.minecraft.data.worldgen.features.OreFeatures
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biome.BiomeCategory
import net.minecraft.world.level.levelgen.GenerationStep.Decoration
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement._
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.BiomeLoadingEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

import java.util

object OreGeneration {
  def oreFeature(metal: MetalEntry, config: OreGenConfigSection): PlacedFeature = {
    val oreConfiguration = new OreConfiguration(
      util.List.of(
        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, metal.block(MetalItemType.OreNormal).defaultBlockState()),
        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, metal.block(MetalItemType.OreDeep).defaultBlockState())
      ),
      config.size()
    )

    val configuredFeature = BuiltinRegistries.register(
      BuiltinRegistries.CONFIGURED_FEATURE,
      new ResourceLocation(AdvTech.ModId, s"ore_${metal.name}"),
      Feature.ORE.configured(oreConfiguration)
    )

    BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(AdvTech.ModId, s"ore_${metal.name}_overworld"),
      configuredFeature.placed(
        CountPlacement.of(config.count()),
        InSquarePlacement.spread(),
        BiomeFilter.biome,
        HeightRangePlacement.uniform(VerticalAnchor.absolute(config.minY()), VerticalAnchor.absolute(config.maxY()))
      )
    )
  }

  var oreFeatureTin: PlacedFeature = _

  def registerFeatures(): Unit = {
    oreFeatureTin = oreFeature(Metals.tin, Config.OreGen.Tin)
  }

  def onCommonSetup(ev: FMLCommonSetupEvent): Unit = {
    ev.enqueueWork((() => {
      registerFeatures()
    }): Runnable)
  }

  def onBiomeLoadingEvent(event: BiomeLoadingEvent): Unit = {
    event.getCategory match {
      case BiomeCategory.NETHER => //pass
      case BiomeCategory.THEEND => //pass
      case _ =>
        event.getGeneration.addFeature(Decoration.UNDERGROUND_ORES, oreFeatureTin)
    }
  }

  def init(): Unit = {
    MinecraftForge.EVENT_BUS.addListener(onBiomeLoadingEvent)
    FMLJavaModLoadingContext.get.getModEventBus.addListener(onCommonSetup)
  }
}
