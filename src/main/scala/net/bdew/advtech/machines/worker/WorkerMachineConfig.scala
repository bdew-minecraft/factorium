package net.bdew.advtech.machines.worker

import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class WorkerMachineConfig(spec: ForgeConfigSpec.Builder, defPowerUse: Float, defCycleTicks: Float) extends ConfigSection {
  val basePowerUsage: () => Float = floatVal(spec, "basePowerUsage",
    "Base power usage (RF/t)", defPowerUse, minVal = 1)

  val baseCycleTicks: () => Float = floatVal(spec, "baseCycleTicks",
    "Base length of cycle (ticks)", defCycleTicks, minVal = 1)

  val baseChargingRate: () => Float = floatVal(spec, "baseChargingRate",
    "Base charging rate (RF/t)", defPowerUse * 10, minVal = 1)

  val basePowerCapacity: () => Float = floatVal(spec, "basePowerCapacity",
    "Base power capacity (RF)", defPowerUse * defCycleTicks * 50, minVal = 1)
}
