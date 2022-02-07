package net.bdew.advtech

import net.bdew.advtech.machines.crusher.CrusherConfig
import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig

object Config {
  private val commonBuilder = new ForgeConfigSpec.Builder

  val Crusher: CrusherConfig = ConfigSection(commonBuilder, "Crusher", new CrusherConfig(commonBuilder))

  val COMMON: ForgeConfigSpec = commonBuilder.build()

  def init(): Unit = {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON)
  }
}
