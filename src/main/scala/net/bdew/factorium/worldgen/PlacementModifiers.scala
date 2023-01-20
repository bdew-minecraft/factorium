package net.bdew.factorium.worldgen

import net.bdew.factorium.worldgen.features.BelowSurfacePlacementType
import net.bdew.lib.managers.RegistryManager
import net.minecraft.core.registries.Registries
import net.minecraftforge.registries.RegistryObject

object PlacementModifiers extends RegistryManager(Registries.PLACEMENT_MODIFIER_TYPE) {
  val belowSurface: RegistryObject[BelowSurfacePlacementType] =
    register("below_surface", () => new BelowSurfacePlacementType)
}
