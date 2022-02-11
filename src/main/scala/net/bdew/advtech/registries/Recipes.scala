package net.bdew.advtech.registries

import net.bdew.advtech.machines.processing.crusher.{CrusherRecipe, CrusherRecipeSerializer}
import net.bdew.lib.managers.RegistryManager
import net.bdew.lib.recipes.MachineRecipeType
import net.minecraftforge.registries.{ForgeRegistries, RegistryObject}

object Recipes extends RegistryManager(ForgeRegistries.RECIPE_SERIALIZERS) {
  val crusherSerializer: RegistryObject[CrusherRecipeSerializer] = register("crusher", () => new CrusherRecipeSerializer)
  val crusherType: MachineRecipeType[CrusherRecipe] = new MachineRecipeType(crusherSerializer)
}

