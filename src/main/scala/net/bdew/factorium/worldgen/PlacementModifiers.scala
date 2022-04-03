package net.bdew.factorium.worldgen

import net.bdew.factorium.worldgen.features.{BelowSurfacePlacementConfigType, CountPlacementConfigType, HeightRangePlacementConfigType}
import net.bdew.lib.managers.VanillaRegistryManager
import net.minecraft.core.Registry
import net.minecraftforge.registries.RegistryObject

object PlacementModifiers extends VanillaRegistryManager(Registry.PLACEMENT_MODIFIERS) {
  val countConfig: RegistryObject[CountPlacementConfigType] =
    register("count_config", () => new CountPlacementConfigType)

  val heightRangeConfig: RegistryObject[HeightRangePlacementConfigType] =
    register("height_range_config", () => new HeightRangePlacementConfigType)

  val belowSurface: RegistryObject[BelowSurfacePlacementConfigType] =
    register("below_surface", () => new BelowSurfacePlacementConfigType)
}
