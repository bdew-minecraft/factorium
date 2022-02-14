package net.bdew.advtech.machines.processing.pulverizer

import net.bdew.advtech.machines.processing.{ProcessingRecipe, ProcessingRecipeSerializer}
import net.bdew.advtech.misc.ItemStackWithChance
import net.bdew.advtech.registries.Recipes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting._

class PulverizerRecipeSerializer extends ProcessingRecipeSerializer[PulverizerRecipe] {
  override def factory(id: ResourceLocation, input: Ingredient, output: ItemStackWithChance, secondary: ItemStackWithChance, bonus: ItemStackWithChance): PulverizerRecipe =
    new PulverizerRecipe(id, input, output, secondary, bonus)
}

class PulverizerRecipe(id: ResourceLocation, input: Ingredient, output: ItemStackWithChance, secondary: ItemStackWithChance, bonus: ItemStackWithChance)
  extends ProcessingRecipe(id, input, output, secondary, bonus) {
  override def getSerializer: RecipeSerializer[_] = Recipes.pulverizerSerializer.get()
  override def getType: RecipeType[_] = Recipes.pulverizerType
}