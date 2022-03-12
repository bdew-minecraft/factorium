package net.bdew.factorium.worldgen

import net.bdew.factorium.worldgen.features.OreFeatureNormal
import net.bdew.lib.managers.RegistryManager
import net.minecraftforge.registries.{ForgeRegistries, RegistryObject}

object Features extends RegistryManager(ForgeRegistries.FEATURES) {
  val oreNormal: RegistryObject[OreFeatureNormal] = register("ore_normal", () => new OreFeatureNormal)
}
