package net.bdew.factorium.registries

import net.bdew.factorium.machines.alloy.{AlloyRecipe, AlloyRecipeSerializer}
import net.bdew.factorium.machines.extruder.{ExtruderRecipe, ExtruderRecipeSerializer}
import net.bdew.factorium.machines.processing.crusher.{CrusherRecipe, CrusherRecipeSerializer}
import net.bdew.factorium.machines.processing.grinder.{GrinderRecipe, GrinderRecipeSerializer}
import net.bdew.factorium.machines.processing.pulverizer.{PulverizerRecipe, PulverizerRecipeSerializer}
import net.bdew.factorium.machines.processing.smelter.{SmelterRecipe, SmelterRecipeSerializer}
import net.bdew.lib.managers.{MachineRecipeDef, RecipeManager}

object Recipes extends RecipeManager {
  val crusher: MachineRecipeDef[CrusherRecipe, CrusherRecipeSerializer] =
    registerMachine("crusher", () => new CrusherRecipeSerializer)

  val grinder: MachineRecipeDef[GrinderRecipe, GrinderRecipeSerializer] =
    registerMachine("grinder", () => new GrinderRecipeSerializer)

  val pulverizer: MachineRecipeDef[PulverizerRecipe, PulverizerRecipeSerializer] =
    registerMachine("pulverizer", () => new PulverizerRecipeSerializer)

  val smelter: MachineRecipeDef[SmelterRecipe, SmelterRecipeSerializer] =
    registerMachine("smelter", () => new SmelterRecipeSerializer)

  val alloy: MachineRecipeDef[AlloyRecipe, AlloyRecipeSerializer] =
    registerMachine("alloy", () => new AlloyRecipeSerializer)

  val extruder: MachineRecipeDef[ExtruderRecipe, ExtruderRecipeSerializer] =
    registerMachine("extruder", () => new ExtruderRecipeSerializer)
}