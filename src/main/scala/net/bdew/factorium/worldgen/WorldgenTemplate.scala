package net.bdew.factorium.worldgen

import net.bdew.lib.config.ConfigSection
import net.minecraft.core.Holder
import net.minecraft.world.level.levelgen.placement._
import net.minecraftforge.common.ForgeConfigSpec

trait WorldgenTemplate[C <: ConfigSection] {
  def id: String
  def makeConfig(spec: ForgeConfigSpec.Builder): C
  def createFeature(cfg: C): Holder[PlacedFeature]
  def isEnabled(cfg: C): Boolean
}

case class WorldgenConfigured[C <: ConfigSection](template: WorldgenTemplate[C], cfg: C) {
  def createPlaced(): WorldgenPlaced = WorldgenPlaced(
    id = template.id,
    featureHolder = template.createFeature(cfg),
    isEnabled = () => template.isEnabled(cfg)
  )
}

case class WorldgenPlaced(id: String, featureHolder: Holder[PlacedFeature], isEnabled: () => Boolean) {
  def feature = featureHolder.value()
}