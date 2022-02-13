package net.bdew.advtech.worldgen

import net.bdew.lib.config.ConfigSection
import net.minecraft.world.level.levelgen.placement._
import net.minecraftforge.common.ForgeConfigSpec

trait OreGenEntry[C <: ConfigSection] {
  def id: String
  def filter: BiomeCatFilter
  def makeConfig(spec: ForgeConfigSpec.Builder): C
  def createFeature(cfg: C): PlacedFeature
}

case class ConfiguredOreGen[C <: ConfigSection](oreGen: OreGenEntry[C], cfg: C) {
  def filter: BiomeCatFilter = oreGen.filter
  def createFeature: PlacedFeature = oreGen.createFeature(cfg)
}

