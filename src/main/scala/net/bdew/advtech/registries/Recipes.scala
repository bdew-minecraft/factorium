package net.bdew.advtech.registries

import net.bdew.advtech.machines.alloy.{AlloyRecipe, AlloyRecipeSerializer}
import net.bdew.advtech.machines.processing.crusher.{CrusherRecipe, CrusherRecipeSerializer}
import net.bdew.advtech.machines.processing.grinder.{GrinderRecipe, GrinderRecipeSerializer}
import net.bdew.advtech.machines.processing.pulverizer.{PulverizerRecipe, PulverizerRecipeSerializer}
import net.bdew.advtech.machines.processing.smelter.{SmelterRecipe, SmelterRecipeSerializer}
import net.bdew.lib.managers.RegistryManager
import net.bdew.lib.recipes.MachineRecipeType
import net.minecraftforge.registries.{ForgeRegistries, RegistryObject}

object Recipes extends RegistryManager(ForgeRegistries.RECIPE_SERIALIZERS) {
  val crusherSerializer: RegistryObject[CrusherRecipeSerializer] = register("crusher", () => new CrusherRecipeSerializer)
  val crusherType: MachineRecipeType[CrusherRecipe] = new MachineRecipeType(crusherSerializer)

  val grinderSerializer: RegistryObject[GrinderRecipeSerializer] = register("grinder", () => new GrinderRecipeSerializer)
  val grinderType: MachineRecipeType[GrinderRecipe] = new MachineRecipeType(grinderSerializer)

  val pulverizerSerializer: RegistryObject[PulverizerRecipeSerializer] = register("pulverizer", () => new PulverizerRecipeSerializer)
  val pulverizerType: MachineRecipeType[PulverizerRecipe] = new MachineRecipeType(pulverizerSerializer)

  val smelterSerializer: RegistryObject[SmelterRecipeSerializer] = register("smelter", () => new SmelterRecipeSerializer)
  val smelterType: MachineRecipeType[SmelterRecipe] = new MachineRecipeType(smelterSerializer)

  val alloySerializer: RegistryObject[AlloyRecipeSerializer] = register("alloy", () => new AlloyRecipeSerializer)
  val alloyType: MachineRecipeType[AlloyRecipe] = new MachineRecipeType(alloySerializer)
}