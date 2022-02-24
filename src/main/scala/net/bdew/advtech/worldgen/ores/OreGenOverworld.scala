package net.bdew.advtech.worldgen.ores

import net.bdew.advtech.AdvTech
import net.bdew.advtech.metals.{MetalEntry, MetalItemType}
import net.bdew.advtech.worldgen.features.{CountPlacementConfig, HeightRangePlacementConfig}
import net.bdew.advtech.worldgen.{BiomeCatFilter, WorldGeneration, WorldgenTemplate}
import net.bdew.lib.config.ConfigSection
import net.minecraft.data.BuiltinRegistries
import net.minecraft.data.worldgen.features.OreFeatures
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement.{BiomeFilter, InSquarePlacement, PlacedFeature}
import net.minecraftforge.common.ForgeConfigSpec

import java.util

case class OreGenOverworld(id: String, metal: MetalEntry, defaultCount: Int, defaultMinY: Int, defaultMaxY: Int, defaultSize: Int, defaultAirExposure: Float) extends WorldgenTemplate[NormalOreGenConfigSection] {
  override def filter: BiomeCatFilter = BiomeCatFilter.normal

  private val normalBlock = metal.addOre(MetalItemType.OreNormal)
  private val deepBlock = metal.addOre(MetalItemType.OreDeep)

  override def makeConfig(spec: ForgeConfigSpec.Builder): NormalOreGenConfigSection =
    ConfigSection(spec, id, new NormalOreGenConfigSection(spec, defaultCount, defaultMinY, defaultMaxY, defaultSize, defaultAirExposure))

  override def isEnabled(cfg: NormalOreGenConfigSection): Boolean = cfg.enabled()

  override def createFeature(cfg: NormalOreGenConfigSection): PlacedFeature = {
    val configuredFeature = BuiltinRegistries.register(
      BuiltinRegistries.CONFIGURED_FEATURE,
      new ResourceLocation(AdvTech.ModId, id),
      WorldGeneration.FEATURE_ORE_NORMAL.configured(
        targets = util.List.of(
          OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, normalBlock.get.defaultBlockState()),
          OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepBlock.get.defaultBlockState())
        ),
        sectionId = id
      )
    )

    BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(AdvTech.ModId, id),
      configuredFeature.placed(
        CountPlacementConfig(id),
        InSquarePlacement.spread(),
        BiomeFilter.biome,
        HeightRangePlacementConfig(id),
      )
    )
  }
}
