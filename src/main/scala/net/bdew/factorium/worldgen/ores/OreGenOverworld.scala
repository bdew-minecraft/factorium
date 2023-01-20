package net.bdew.factorium.worldgen.ores

import net.bdew.factorium.metals.{MetalEntry, MetalItemType}
import net.bdew.factorium.worldgen.WorldgenType
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest

case class OreGenOverworld(id: String, metal: MetalEntry, defaultCount: Int, defaultMinY: Int, defaultMaxY: Int, defaultSize: Int, defaultAirExposure: Float) extends OreGenNormal {
  override def worldgenType: WorldgenType = WorldgenType.OresOverworld

  private val normalBlock = metal.addOre(MetalItemType.OreNormal)
  private val deepBlock = metal.addOre(MetalItemType.OreDeep)

  val normalReplace = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES)
  val deepReplace = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES)

  def oreTargets: List[TargetBlockState] = List(
    OreConfiguration.target(normalReplace, normalBlock.get.defaultBlockState()),
    OreConfiguration.target(deepReplace, deepBlock.get.defaultBlockState())
  )
}
