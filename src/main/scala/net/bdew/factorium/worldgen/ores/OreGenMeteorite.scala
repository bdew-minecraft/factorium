package net.bdew.factorium.worldgen.ores

import net.bdew.factorium.Factorium
import net.bdew.factorium.registries.Blocks
import net.bdew.factorium.worldgen.features.{BelowSurfacePlacementConfig, CountPlacementConfig}
import net.bdew.factorium.worldgen.{BiomeCatFilter, Features, WorldgenTemplate}
import net.bdew.lib.config.ConfigSection
import net.minecraft.core.Holder
import net.minecraft.data.BuiltinRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{BlockTags, TagKey}
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState
import net.minecraft.world.level.levelgen.placement.{BiomeFilter, InSquarePlacement, PlacedFeature}
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
import net.minecraftforge.common.ForgeConfigSpec

import scala.jdk.CollectionConverters._

object OreGenMeteorite extends WorldgenTemplate[DepthOreGenConfigSection] {
  override def id: String = "meteorite_overworld"
  override val filter: BiomeCatFilter = BiomeCatFilter.normal

  val replaceables: TagKey[Block] =
    BlockTags.create(new ResourceLocation(Factorium.ModId, "meteorite_ore_replaceables"))

  override def makeConfig(spec: ForgeConfigSpec.Builder): DepthOreGenConfigSection =
    ConfigSection(spec, id, new DepthOreGenConfigSection(spec,
      defaultCount = 3,
      defaultMinDepth = 5,
      defaultMaxDepth = 30,
      defaultSize = 9,
      defaultAirExposure = 0))

  override def isEnabled(cfg: DepthOreGenConfigSection): Boolean = cfg.enabled()

  def oreTargets: List[TargetBlockState] = List(
    OreConfiguration.target(new TagMatchTest(replaceables), Blocks.meteoriteOre.block.get().defaultBlockState()),
  )

  override def createFeature(cfg: DepthOreGenConfigSection): Holder[PlacedFeature] = {
    val configuredFeature = BuiltinRegistries.register(
      BuiltinRegistries.CONFIGURED_FEATURE,
      new ResourceLocation(Factorium.ModId, id),
      Features.oreNormal.get.configured(oreTargets.asJava, id)
    )

    BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(Factorium.ModId, id),
      new PlacedFeature(configuredFeature, List(
        CountPlacementConfig(id),
        InSquarePlacement.spread(),
        BiomeFilter.biome,
        BelowSurfacePlacementConfig(id),
      ).asJava)
    )
  }
}
