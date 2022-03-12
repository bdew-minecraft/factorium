package net.bdew.factorium.machines.processing.pulverizer

import net.bdew.factorium.machines.processing.{ProcessingRecipe, ProcessingRecipeSerializer}
import net.bdew.factorium.misc.ItemStackWithChance
import net.bdew.factorium.registries.Recipes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting._

class PulverizerRecipeSerializer extends ProcessingRecipeSerializer[PulverizerRecipe] {
  override def factory(id: ResourceLocation, input: Ingredient, output: ItemStackWithChance, secondary: ItemStackWithChance, bonus: ItemStackWithChance): PulverizerRecipe =
    new PulverizerRecipe(id, input, output, secondary, bonus)
}

class PulverizerRecipe(id: ResourceLocation, input: Ingredient, output: ItemStackWithChance, secondary: ItemStackWithChance, bonus: ItemStackWithChance)
  extends ProcessingRecipe(id, input, output, secondary, bonus) {
  override def getSerializer: RecipeSerializer[_] = Recipes.pulverizer.serializer
  override def getType: RecipeType[_] = Recipes.pulverizer.recipeType
}