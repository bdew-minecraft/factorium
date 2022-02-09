package net.bdew.advtech.worldgen

import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class OreGenConfigSection(spec: ForgeConfigSpec.Builder, defaultCount: Int, defaultMinY: Int, defaultMaxY: Int, defaultSize: Int) extends ConfigSection {
  val count: () => Int = intVal(spec, "count", "Number of veins to generate, set to 0 to disable", defaultCount, minVal = 0)
  val minY: () => Int = intVal(spec, "minY", "Minimum height", defaultMinY)
  val maxY: () => Int = intVal(spec, "maxY", "Maximum height", defaultMaxY)
  val size: () => Int = intVal(spec, "size", "Vein size", defaultSize, minVal = 1)
}

class OreGenConfig(spec: ForgeConfigSpec.Builder) extends ConfigSection {
  val Tin: OreGenConfigSection = ConfigSection(spec, "Tin", new OreGenConfigSection(spec, 20, -30, 100, 10))
}