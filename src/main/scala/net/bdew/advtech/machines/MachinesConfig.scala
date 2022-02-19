package net.bdew.advtech.machines

import net.bdew.advtech.machines.alloy.AlloySmelterConfig
import net.bdew.advtech.machines.processing.crusher.CrusherConfig
import net.bdew.advtech.machines.processing.grinder.GrinderConfig
import net.bdew.advtech.machines.processing.pulverizer.PulverizerConfig
import net.bdew.advtech.machines.processing.smelter.SmelterConfig
import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class MachinesConfig(spec: ForgeConfigSpec.Builder) extends ConfigSection {
  val Crusher: CrusherConfig = ConfigSection(spec, "Crusher", new CrusherConfig(spec))
  val Grinder: GrinderConfig = ConfigSection(spec, "Grinder", new GrinderConfig(spec))
  val Pulverizer: PulverizerConfig = ConfigSection(spec, "Pulverizer", new PulverizerConfig(spec))
  val Smelter: SmelterConfig = ConfigSection(spec, "Smelter", new SmelterConfig(spec))
  val AlloySmelter: AlloySmelterConfig = ConfigSection(spec, "AlloySmelter", new AlloySmelterConfig(spec))
}
