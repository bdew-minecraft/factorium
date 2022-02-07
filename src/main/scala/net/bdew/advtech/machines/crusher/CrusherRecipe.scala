package net.bdew.advtech.machines.crusher

import com.google.gson.JsonObject
import net.bdew.advtech.misc.ItemStackWithChance
import net.bdew.advtech.registries.Recipes
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.{Ingredient, RecipeSerializer, RecipeType}

class CrusherRecipeSerializer extends BaseMachineRecipeSerializer[CrusherRecipe] {
  override def fromJson(recipeId: ResourceLocation, obj: JsonObject): CrusherRecipe = {
    val input = Ingredient.fromJson(obj.get("input"))
    val output = ItemStackWithChance.fromJson(obj.getAsJsonObject("output"))
    val secondary = ItemStackWithChance.fromJsonOpt(obj, "secondary")
    val bonus = ItemStackWithChance.fromJsonOpt(obj, "bonus")
    new CrusherRecipe(recipeId, input, output, secondary, bonus)
  }

  override def fromNetwork(recipeId: ResourceLocation, buff: FriendlyByteBuf): CrusherRecipe = {
    val input = Ingredient.fromNetwork(buff)
    val output = ItemStackWithChance.fromNetwork(buff)
    val secondary = ItemStackWithChance.fromNetwork(buff)
    val bonus = ItemStackWithChance.fromNetwork(buff)
    new CrusherRecipe(recipeId, input, output, secondary, bonus)
  }

  override def toNetwork(buffer: FriendlyByteBuf, recipe: CrusherRecipe): Unit = {
    recipe.input.toNetwork(buffer)
    recipe.output.toNetwork(buffer)
    recipe.secondary.toNetwork(buffer)
    recipe.bonus.toNetwork(buffer)
  }
}

class CrusherRecipe(id: ResourceLocation, val input: Ingredient, val output: ItemStackWithChance, val secondary: ItemStackWithChance, val bonus: ItemStackWithChance) extends BaseMachineRecipe(id) {
  override def getSerializer: RecipeSerializer[_] = Recipes.crusherSerializer.get()
  override def getType: RecipeType[_] = Recipes.crusherType
}
