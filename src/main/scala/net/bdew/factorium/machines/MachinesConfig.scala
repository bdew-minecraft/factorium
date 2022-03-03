package net.bdew.factorium.machines

import net.bdew.factorium.machines.alloy.AlloySmelterConfig
import net.bdew.factorium.machines.extruder.ExtruderConfig
import net.bdew.factorium.machines.processing.crusher.CrusherConfig
import net.bdew.factorium.machines.processing.grinder.GrinderConfig
import net.bdew.factorium.machines.processing.pulverizer.PulverizerConfig
import net.bdew.factorium.machines.processing.smelter.SmelterConfig
import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class MachinesConfig(spec: ForgeConfigSpec.Builder) extends ConfigSection {
  val Crusher: CrusherConfig = ConfigSection(spec, "Crusher", new CrusherConfig(spec))
  val Grinder: GrinderConfig = ConfigSection(spec, "Grinder", new GrinderConfig(spec))
  val Pulverizer: PulverizerConfig = ConfigSection(spec, "Pulverizer", new PulverizerConfig(spec))
  val Smelter: SmelterConfig = ConfigSection(spec, "Smelter", new SmelterConfig(spec))
  val AlloySmelter: AlloySmelterConfig = ConfigSection(spec, "AlloySmelter", new AlloySmelterConfig(spec))
  val Extruder: ExtruderConfig = ConfigSection(spec, "Extruder", new ExtruderConfig(spec))
}
