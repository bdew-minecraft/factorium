package net.bdew.advtech.worldgen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.metals.{MetalEntry, MetalItemType}
import net.bdew.lib.config.ConfigSection
import net.minecraft.data.BuiltinRegistries
import net.minecraft.data.worldgen.features.OreFeatures
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement._
import net.minecraftforge.common.ForgeConfigSpec

import java.util

case class OreGenOverworld(id: String, metal: MetalEntry, defaultCount: Int, defaultMinY: Int, defaultMaxY: Int, defaultSize: Int) extends OreGenEntry[NormalOreGenConfigSection] {
  override def filter: BiomeCatFilter = BiomeCatFilter.normal

  private val normalBlock = metal.addOre(MetalItemType.OreNormal)
  private val deepBlock = metal.addOre(MetalItemType.OreDeep)

  override def makeConfig(spec: ForgeConfigSpec.Builder): NormalOreGenConfigSection =
    ConfigSection(spec, id, new NormalOreGenConfigSection(spec, defaultCount, defaultMinY, defaultMaxY, defaultSize))

  override def createFeature(cfg: NormalOreGenConfigSection): PlacedFeature = {
    val oreConfiguration = new OreConfiguration(
      util.List.of(
        OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, normalBlock.get.defaultBlockState()),
        OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepBlock.get.defaultBlockState())
      ),
      cfg.size()
    )

    val configuredFeature = BuiltinRegistries.register(
      BuiltinRegistries.CONFIGURED_FEATURE,
      new ResourceLocation(AdvTech.ModId, id),
      Feature.ORE.configured(oreConfiguration)
    )

    BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(AdvTech.ModId, id),
      configuredFeature.placed(
        CountPlacement.of(cfg.count()),
        InSquarePlacement.spread(),
        BiomeFilter.biome,
        HeightRangePlacement.uniform(VerticalAnchor.absolute(cfg.minY()), VerticalAnchor.absolute(cfg.maxY()))
      )
    )
  }
}
