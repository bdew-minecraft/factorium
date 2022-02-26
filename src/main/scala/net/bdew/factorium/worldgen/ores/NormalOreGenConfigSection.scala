package net.bdew.factorium.worldgen.ores

import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class NormalOreGenConfigSection(spec: ForgeConfigSpec.Builder, defaultCount: Int, defaultMinY: Int, defaultMaxY: Int, defaultSize: Int, defaultAirExposure: Float) extends ConfigSection {
  val enabled: () => Boolean = boolVal(spec, "enabled", "Set to false to disable generation", true)
  val count: () => Int = intVal(spec, "count", "Number of veins to generate", defaultCount, minVal = 1)
  val airExposure: () => Float = floatVal(spec, "airExposure", "Chance of vein to NOT be generated when exposed to air", defaultAirExposure, minVal = 0, maxVal = 1)
  val minY: () => Int = intVal(spec, "minY", "Minimum height", defaultMinY)
  val maxY: () => Int = intVal(spec, "maxY", "Maximum height", defaultMaxY)
  val size: () => Int = intVal(spec, "size", "Vein size", defaultSize, minVal = 1)
}
