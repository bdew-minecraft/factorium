package net.bdew.advtech.worldgen.ores

import net.bdew.advtech.metals.{MetalEntry, MetalItemType}
import net.bdew.advtech.worldgen.BiomeCatFilter
import net.minecraft.data.worldgen.features.OreFeatures
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState

case class OreGenOverworld(id: String, metal: MetalEntry, defaultCount: Int, defaultMinY: Int, defaultMaxY: Int, defaultSize: Int, defaultAirExposure: Float) extends OreGenNormal {
  override def filter: BiomeCatFilter = BiomeCatFilter.normal

  private val normalBlock = metal.addOre(MetalItemType.OreNormal)
  private val deepBlock = metal.addOre(MetalItemType.OreDeep)

  def oreTargets: List[TargetBlockState] = List(
    OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, normalBlock.get.defaultBlockState()),
    OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepBlock.get.defaultBlockState())
  )
}
