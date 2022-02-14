package net.bdew.advtech.machines.processing.grinder

import net.bdew.advtech.machines.processing.{ProcessingRecipe, ProcessingRecipeSerializer}
import net.bdew.advtech.misc.ItemStackWithChance
import net.bdew.advtech.registries.Recipes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting._

class GrinderRecipeSerializer extends ProcessingRecipeSerializer[GrinderRecipe] {
  override def factory(id: ResourceLocation, input: Ingredient, output: ItemStackWithChance, secondary: ItemStackWithChance, bonus: ItemStackWithChance): GrinderRecipe =
    new GrinderRecipe(id, input, output, secondary, bonus)
}

class GrinderRecipe(id: ResourceLocation, input: Ingredient, output: ItemStackWithChance, secondary: ItemStackWithChance, bonus: ItemStackWithChance)
  extends ProcessingRecipe(id, input, output, secondary, bonus) {
  override def getSerializer: RecipeSerializer[_] = Recipes.grinderSerializer.get()
  override def getType: RecipeType[_] = Recipes.grinderType
}