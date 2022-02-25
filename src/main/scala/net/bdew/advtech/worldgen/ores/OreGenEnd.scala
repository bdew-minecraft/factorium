package net.bdew.advtech.worldgen.ores

import net.bdew.advtech.metals.{MetalEntry, MetalItemType}
import net.bdew.advtech.worldgen.BiomeCatFilter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest

case class OreGenEnd(id: String, metal: MetalEntry, defaultCount: Int, defaultMinY: Int, defaultMaxY: Int, defaultSize: Int, defaultAirExposure: Float) extends OreGenNormal {
  override def filter: BiomeCatFilter = BiomeCatFilter.end

  private val block = metal.addOre(MetalItemType.OreEnd)

  def oreTargets: List[TargetBlockState] = List(
    OreConfiguration.target(new BlockMatchTest(Blocks.END_STONE), block.get.defaultBlockState()),
  )
}
