package net.bdew.advtech.machines.crusher

import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class CrusherConfig(spec: ForgeConfigSpec.Builder) extends ConfigSection {
  val basePowerUsage: () => Float = floatVal(spec, "basePowerUsage",
    "Base power usage (RF/t)", 50, minVal = 1)

  val baseCycleTicks: () => Float = floatVal(spec, "baseCycleTicks",
    "Base length of cycle (ticks)", 100, minVal = 1)

  val baseChargingRate: () => Float = floatVal(spec, "baseChargingRate",
    "Base charging rate (RF/t)", 1000, minVal = 1)

  val basePowerCapacity: () => Float = floatVal(spec, "basePowerCapacity",
    "Base power capacity (RF)", 200000, minVal = 1)
}
