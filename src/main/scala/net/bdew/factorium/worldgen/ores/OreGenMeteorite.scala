package net.bdew.factorium.worldgen.ores

import net.bdew.factorium.Factorium
import net.bdew.factorium.registries.Blocks
import net.bdew.factorium.worldgen.features.BelowSurfacePlacement
import net.bdew.factorium.worldgen.{WorldgenTemplate, WorldgenType}
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{BlockTags, TagKey}
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState
import net.minecraft.world.level.levelgen.feature.{ConfiguredFeature, Feature}
import net.minecraft.world.level.levelgen.placement.{BiomeFilter, CountPlacement, InSquarePlacement, PlacedFeature}
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest

import scala.jdk.CollectionConverters._

object OreGenMeteorite extends WorldgenTemplate {
  override def id: String = "meteorite_overworld"
  override def worldgenType: WorldgenType = WorldgenType.OresOverworld

  val replaceables: TagKey[Block] =
    BlockTags.create(new ResourceLocation(Factorium.ModId, "meteorite_ore_replaceables"))

  val defaultCount = 3
  val defaultMinDepth = 5
  val defaultMaxDepth = 30
  val defaultSize = 9
  val defaultAirExposure = 0

  def oreTargets: List[TargetBlockState] = List(
    OreConfiguration.target(new TagMatchTest(replaceables), Blocks.meteoriteOre.block.get().defaultBlockState()),
  )

  override def makeConfiguredFeature: ConfiguredFeature[_, _] = {
    new ConfiguredFeature(Feature.ORE, new OreConfiguration(
      oreTargets.asJava,
      defaultSize,
      defaultAirExposure
    ))
  }

  override def makePlacedFeature(feature: Holder[ConfiguredFeature[_, _]]): PlacedFeature =
    new PlacedFeature(feature, List(
      CountPlacement.of(defaultCount),
      InSquarePlacement.spread(),
      BelowSurfacePlacement(defaultMinDepth, defaultMaxDepth),
      BiomeFilter.biome()
    ).asJava)
}
