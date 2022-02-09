package net.bdew.advtech.machines

import net.bdew.advtech.machines.crusher.CrusherConfig
import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class MachinesConfig(spec: ForgeConfigSpec.Builder) extends ConfigSection {
  val Crusher: CrusherConfig = ConfigSection(spec, "Crusher", new CrusherConfig(spec))
}
