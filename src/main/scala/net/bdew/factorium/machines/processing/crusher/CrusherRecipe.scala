package net.bdew.factorium.machines.processing.crusher

import net.bdew.factorium.machines.processing.{ProcessingRecipe, ProcessingRecipeSerializer}
import net.bdew.factorium.misc.ItemStackWithChance
import net.bdew.factorium.registries.Recipes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.{Ingredient, RecipeSerializer, RecipeType}

class CrusherRecipeSerializer extends ProcessingRecipeSerializer[CrusherRecipe] {
  override def factory(id: ResourceLocation, input: Ingredient, output: ItemStackWithChance, secondary: ItemStackWithChance, bonus: ItemStackWithChance): CrusherRecipe =
    new CrusherRecipe(id, input, output, secondary, bonus)
}

class CrusherRecipe(id: ResourceLocation, input: Ingredient, output: ItemStackWithChance, secondary: ItemStackWithChance, bonus: ItemStackWithChance)
  extends ProcessingRecipe(id, input, output, secondary, bonus) {
  override def getSerializer: RecipeSerializer[_] = Recipes.crusher.serializer
  override def getType: RecipeType[_] = Recipes.crusher.recipeType
}