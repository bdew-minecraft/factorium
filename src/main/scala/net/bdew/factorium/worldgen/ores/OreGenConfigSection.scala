package net.bdew.factorium.worldgen.ores

import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

trait OreGenConfigSection extends ConfigSection {
  def enabled: () => Boolean
  def count: () => Int
  def airExposure: () => Float
  def size: () => Int
}

class NormalOreGenConfigSection(spec: ForgeConfigSpec.Builder, defaultCount: Int, defaultMinY: Int, defaultMaxY: Int, defaultSize: Int, defaultAirExposure: Float) extends OreGenConfigSection {
  override val enabled: () => Boolean = boolVal(spec, "enabled", "Set to false to disable generation", true)
  override val count: () => Int = intVal(spec, "count", "Number of veins to generate", defaultCount, minVal = 1)
  override val airExposure: () => Float = floatVal(spec, "airExposure", "Chance of vein to NOT be generated when exposed to air", defaultAirExposure, minVal = 0, maxVal = 1)
  val minY: () => Int = intVal(spec, "minY", "Minimum height", defaultMinY)
  val maxY: () => Int = intVal(spec, "maxY", "Maximum height", defaultMaxY)
  override val size: () => Int = intVal(spec, "size", "Vein size", defaultSize, minVal = 1)
}

class DepthOreGenConfigSection(spec: ForgeConfigSpec.Builder, defaultCount: Int, defaultMinDepth: Int, defaultMaxDepth: Int, defaultSize: Int, defaultAirExposure: Float) extends OreGenConfigSection {
  override val enabled: () => Boolean = boolVal(spec, "enabled", "Set to false to disable generation", true)
  override val count: () => Int = intVal(spec, "count", "Number of veins to generate", defaultCount, minVal = 1)
  override val airExposure: () => Float = floatVal(spec, "airExposure", "Chance of vein to NOT be generated when exposed to air", defaultAirExposure, minVal = 0, maxVal = 1)
  val minDepth: () => Int = intVal(spec, "minY", "Minimum depth under surface", defaultMinDepth, minVal = 0)
  val maxDepth: () => Int = intVal(spec, "maxY", "Maximum depth under surface", defaultMaxDepth, minVal = 0)
  override val size: () => Int = intVal(spec, "size", "Vein size", defaultSize, minVal = 1)
}
