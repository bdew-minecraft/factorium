package net.bdew.factorium.worldgen

import net.bdew.factorium.worldgen.features.{BelowSurfacePlacementConfigType, CountPlacementConfigType, HeightRangePlacementConfigType}
import net.bdew.lib.managers.{VanillaRegistryManager, VanillaRegistryObject}
import net.minecraft.core.Registry

object PlacementModifiers extends VanillaRegistryManager(Registry.PLACEMENT_MODIFIERS) {
  val countConfig: VanillaRegistryObject[CountPlacementConfigType] =
    register("count_config", () => new CountPlacementConfigType)

  val heightRangeConfig: VanillaRegistryObject[HeightRangePlacementConfigType] =
    register("height_range_config", () => new HeightRangePlacementConfigType)

  val belowSurface: VanillaRegistryObject[BelowSurfacePlacementConfigType] =
    register("below_surface", () => new BelowSurfacePlacementConfigType)
}
