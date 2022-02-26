package net.bdew.factorium.worldgen.ores

import net.bdew.factorium.Factorium
import net.bdew.factorium.metals.MetalEntry
import net.bdew.factorium.worldgen.features.{CountPlacementConfig, HeightRangePlacementConfig}
import net.bdew.factorium.worldgen.{WorldGeneration, WorldgenTemplate}
import net.bdew.lib.config.ConfigSection
import net.minecraft.data.BuiltinRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState
import net.minecraft.world.level.levelgen.placement.{BiomeFilter, InSquarePlacement, PlacedFeature}
import net.minecraftforge.common.ForgeConfigSpec

import scala.jdk.CollectionConverters._

abstract class OreGenNormal extends WorldgenTemplate[NormalOreGenConfigSection] {
  def id: String
  def metal: MetalEntry
  def defaultCount: Int
  def defaultMinY: Int
  def defaultMaxY: Int
  def defaultSize: Int
  def defaultAirExposure: Float

  override def makeConfig(spec: ForgeConfigSpec.Builder): NormalOreGenConfigSection =
    ConfigSection(spec, id, new NormalOreGenConfigSection(spec, defaultCount, defaultMinY, defaultMaxY, defaultSize, defaultAirExposure))

  override def isEnabled(cfg: NormalOreGenConfigSection): Boolean = cfg.enabled()

  def oreTargets: List[TargetBlockState]

  override def createFeature(cfg: NormalOreGenConfigSection): PlacedFeature = {
    val configuredFeature = BuiltinRegistries.register(
      BuiltinRegistries.CONFIGURED_FEATURE,
      new ResourceLocation(Factorium.ModId, id),
      WorldGeneration.FEATURE_ORE_NORMAL.configured(oreTargets.asJava, id)
    )

    BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(Factorium.ModId, id),
      configuredFeature.placed(
        CountPlacementConfig(id),
        InSquarePlacement.spread(),
        BiomeFilter.biome,
        HeightRangePlacementConfig(id),
      )
    )
  }

}
