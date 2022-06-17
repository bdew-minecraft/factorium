package net.bdew.factorium.worldgen

import net.bdew.factorium.worldgen.features.BelowSurfacePlacementType
import net.bdew.lib.managers.VanillaRegistryManager
import net.minecraft.core.Registry
import net.minecraftforge.registries.RegistryObject

object PlacementModifiers extends VanillaRegistryManager(Registry.PLACEMENT_MODIFIERS) {
  val belowSurface: RegistryObject[BelowSurfacePlacementType] =
    register("below_surface", () => new BelowSurfacePlacementType)
}
