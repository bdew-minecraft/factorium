package net.bdew.factorium.worldgen

import net.minecraft.core.Holder
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState
import net.minecraft.world.level.levelgen.placement._

trait WorldgenTemplate {
  def id: String
  def worldgenType: WorldgenType
  def oreTargets: List[TargetBlockState]
  def makeConfiguredFeature: ConfiguredFeature[_, _]
  def makePlacedFeature(feature: Holder[ConfiguredFeature[_, _]]): PlacedFeature
}
