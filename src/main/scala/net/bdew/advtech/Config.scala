package net.bdew.advtech

import net.bdew.advtech.machines.MachinesConfig
import net.bdew.advtech.worldgen.WorldGenConfig
import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig

object Config {
  private val commonBuilder = new ForgeConfigSpec.Builder
  private val serverBuilder = new ForgeConfigSpec.Builder

  val WorldGen: WorldGenConfig = ConfigSection(serverBuilder, "WorldGen", new WorldGenConfig(serverBuilder))

  val Machines: MachinesConfig = ConfigSection(commonBuilder, "Machines", new MachinesConfig(commonBuilder))

  val COMMON: ForgeConfigSpec = commonBuilder.build()
  val SERVER: ForgeConfigSpec = serverBuilder.build()

  def init(): Unit = {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON)
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER)
  }
}
