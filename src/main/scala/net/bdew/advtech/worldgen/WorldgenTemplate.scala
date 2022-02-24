package net.bdew.advtech.worldgen

import net.bdew.lib.config.ConfigSection
import net.minecraft.world.level.levelgen.placement._
import net.minecraftforge.common.ForgeConfigSpec

trait WorldgenTemplate[C <: ConfigSection] {
  def id: String
  def filter: BiomeCatFilter
  def makeConfig(spec: ForgeConfigSpec.Builder): C
  def createFeature(cfg: C): PlacedFeature
  def isEnabled(cfg: C): Boolean
}

case class WorldgenConfigured[C <: ConfigSection](template: WorldgenTemplate[C], cfg: C) {
  def createPlaced(): WorldgenPlaced = WorldgenPlaced(
    id = template.id,
    filter = template.filter,
    feature = template.createFeature(cfg),
    isEnabled = () => template.isEnabled(cfg)
  )
}

case class WorldgenPlaced(id: String, filter: BiomeCatFilter, feature: PlacedFeature, isEnabled: () => Boolean)