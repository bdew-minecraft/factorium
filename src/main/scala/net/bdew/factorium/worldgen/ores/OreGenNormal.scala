package net.bdew.factorium.worldgen.ores

import net.bdew.factorium.metals.MetalEntry
import net.bdew.factorium.worldgen.WorldgenTemplate
import net.minecraft.core.Holder
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.{ConfiguredFeature, Feature}
import net.minecraft.world.level.levelgen.placement._

import scala.jdk.CollectionConverters._

abstract class OreGenNormal extends WorldgenTemplate {
  def id: String
  def metal: MetalEntry
  def defaultCount: Int
  def defaultMinY: Int
  def defaultMaxY: Int
  def defaultSize: Int
  def defaultAirExposure: Float

  override def makeConfiguredFeature: ConfiguredFeature[_, _] =
    new ConfiguredFeature(Feature.ORE, new OreConfiguration(
      oreTargets.asJava,
      defaultSize,
      defaultAirExposure
    ))

  override def makePlacedFeature(feature: Holder[ConfiguredFeature[_, _]]): PlacedFeature =
    new PlacedFeature(feature, List(
      CountPlacement.of(defaultCount),
      InSquarePlacement.spread(),
      HeightRangePlacement.uniform(VerticalAnchor.absolute(defaultMinY), VerticalAnchor.absolute(defaultMaxY)),
      BiomeFilter.biome()
    ).asJava)
}
