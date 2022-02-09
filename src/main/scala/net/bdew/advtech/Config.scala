package net.bdew.advtech

import net.bdew.advtech.machines.MachinesConfig
import net.bdew.advtech.worldgen.OreGenConfig
import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig

object Config {
  private val commonBuilder = new ForgeConfigSpec.Builder

  val OreGen: OreGenConfig = ConfigSection(commonBuilder, "OreGeneration", new OreGenConfig(commonBuilder))
  val Machines: MachinesConfig = ConfigSection(commonBuilder, "Machines", new MachinesConfig(commonBuilder))

  val COMMON: ForgeConfigSpec = commonBuilder.build()

  def init(): Unit = {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON)
  }
}
