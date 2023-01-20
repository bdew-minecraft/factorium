package net.bdew.factorium.worldgen.ores

import net.bdew.factorium.metals.{MetalEntry, MetalItemType}
import net.bdew.factorium.worldgen.WorldgenType
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest

case class OreGenNether(id: String, metal: MetalEntry, defaultCount: Int, defaultMinY: Int, defaultMaxY: Int, defaultSize: Int, defaultAirExposure: Float) extends OreGenNormal {
  override def worldgenType: WorldgenType = WorldgenType.OresNether

  private val block = metal.addOre(MetalItemType.OreNether)

  val replaceables = new TagMatchTest(BlockTags.BASE_STONE_NETHER)

  def oreTargets: List[TargetBlockState] = List(
    OreConfiguration.target(replaceables, block.get.defaultBlockState()),
  )
}
