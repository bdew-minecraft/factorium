package net.bdew.factorium.machines.pump

import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class PumpConfig(spec: ForgeConfigSpec.Builder) extends ConfigSection {
  val tankCapacity: () => Int = intVal(spec, "tankCapacity",
    "Tank capacity (mB)", 32000, minVal = 1000)

  val basePowerUsage: () => Float = floatVal(spec, "basePowerUsage",
    "Base power usage (RF/t)", 10, minVal = 1)

  val baseCycleTicks: () => Float = floatVal(spec, "baseCycleTicks",
    "Base length of cycle (ticks)", 40, minVal = 1)

  val baseChargingRate: () => Float = floatVal(spec, "baseChargingRate",
    "Base charging rate (RF/t)", 20, minVal = 1)

  val basePowerCapacity: () => Float = floatVal(spec, "basePowerCapacity",
    "Base power capacity (RF)", 50000, minVal = 1)

  val scanPerTick: () => Int = intVal(spec, "scanPerTick",
    "Blocks to scan per tick", 100, minVal = 1)

  val maxPumpDistance: () => Int = intVal(spec, "maxPumpDistance",
    "Maximum distance to pump", 100, minVal = 1)

  val infiniteThreshold: () => Int = intVal(spec, "infiniteThreshold",
    "If a fluid has over this amount of blocks on a single Y level - it will be considered infinite", 5000, minVal = 1)

}