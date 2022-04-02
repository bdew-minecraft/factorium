package net.bdew.factorium.machines.mixer

import net.bdew.factorium.machines.worker.WorkerMachineConfig
import net.minecraftforge.common.ForgeConfigSpec

class MixerConfig(spec: ForgeConfigSpec.Builder) extends WorkerMachineConfig(spec, 20, 100) {
  val tankCapacity: () => Int = intVal(spec, "tankCapacity",
    "Tank capacity (mB)", 32000, minVal = 1000)
}


