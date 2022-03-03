package net.bdew.factorium.registries

import net.bdew.factorium.machines.alloy.{AlloyRecipe, AlloyRecipeSerializer}
import net.bdew.factorium.machines.extruder.{ExtruderRecipe, ExtruderRecipeSerializer}
import net.bdew.factorium.machines.processing.crusher.{CrusherRecipe, CrusherRecipeSerializer}
import net.bdew.factorium.machines.processing.grinder.{GrinderRecipe, GrinderRecipeSerializer}
import net.bdew.factorium.machines.processing.pulverizer.{PulverizerRecipe, PulverizerRecipeSerializer}
import net.bdew.factorium.machines.processing.smelter.{SmelterRecipe, SmelterRecipeSerializer}
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

  val extruderSerializer: RegistryObject[ExtruderRecipeSerializer] = register("extruder", () => new ExtruderRecipeSerializer)
  val extruderType: MachineRecipeType[ExtruderRecipe] = new MachineRecipeType(extruderSerializer)
}