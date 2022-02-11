package net.bdew.advtech.machines

import net.bdew.advtech.machines.processing.crusher.CrusherRecipe
import net.bdew.advtech.registries.Recipes
import net.bdew.lib.recipes.RecipeReloadListener
import net.minecraft.world.item.crafting.RecipeManager

object MachineRecipes {
  var crusher = Set.empty[CrusherRecipe]

  def refreshRecipes(manager: RecipeManager): Unit = {
    crusher = Recipes.crusherType.getAllRecipes(manager).toSet
  }

  def init(): Unit = {
    RecipeReloadListener.onServerRecipeUpdate.listen(refreshRecipes)
    RecipeReloadListener.onClientRecipeUpdate.listen(refreshRecipes)
  }
}
