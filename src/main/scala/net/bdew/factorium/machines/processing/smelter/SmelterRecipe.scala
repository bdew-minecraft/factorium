package net.bdew.factorium.machines.processing.smelter

import net.bdew.factorium.machines.processing.{ProcessingRecipe, ProcessingRecipeSerializer}
import net.bdew.factorium.misc.ItemStackWithChance
import net.bdew.factorium.registries.Recipes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting._

class SmelterRecipeSerializer extends ProcessingRecipeSerializer[SmelterRecipe] {
  override def factory(id: ResourceLocation, input: Ingredient, output: ItemStackWithChance, secondary: ItemStackWithChance, bonus: ItemStackWithChance): SmelterRecipe =
    new SmelterRecipe(id, input, output, secondary, bonus)
}

class SmelterRecipe(id: ResourceLocation, input: Ingredient, output: ItemStackWithChance, secondary: ItemStackWithChance, bonus: ItemStackWithChance)
  extends ProcessingRecipe(id, input, output, secondary, bonus) {
  override def getSerializer: RecipeSerializer[_] = Recipes.smelterSerializer.get()
  override def getType: RecipeType[_] = Recipes.smelterType
}