package net.bdew.factorium.worldgen.ores

import net.bdew.factorium.metals.{MetalEntry, MetalItemType}
import net.minecraft.data.worldgen.features.OreFeatures
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState

case class OreGenNether(id: String, metal: MetalEntry, defaultCount: Int, defaultMinY: Int, defaultMaxY: Int, defaultSize: Int, defaultAirExposure: Float) extends OreGenNormal {
  private val block = metal.addOre(MetalItemType.OreNether)

  def oreTargets: List[TargetBlockState] = List(
    OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, block.get.defaultBlockState()),
  )
}
