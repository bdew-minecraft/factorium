package net.bdew.advtech.machines.processing

import com.google.gson.JsonObject
import net.bdew.advtech.misc.ItemStackWithChance
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

abstract class ProcessingRecipe(id: ResourceLocation,
                                val input: Ingredient,
                                val output: ItemStackWithChance,
                                val secondary: ItemStackWithChance,
                                val bonus: ItemStackWithChance
                               ) extends BaseMachineRecipe(id)

abstract class ProcessingRecipeSerializer[T <: ProcessingRecipe] extends BaseMachineRecipeSerializer[T] {
  def factory(id: ResourceLocation, input: Ingredient, output: ItemStackWithChance, secondary: ItemStackWithChance, bonus: ItemStackWithChance): T

  override def fromJson(recipeId: ResourceLocation, obj: JsonObject): T = {
    val input = Ingredient.fromJson(obj.get("input"))
    val output = ItemStackWithChance.fromJson(obj.getAsJsonObject("output"))
    val secondary = ItemStackWithChance.fromJsonOpt(obj, "secondary")
    val bonus = ItemStackWithChance.fromJsonOpt(obj, "bonus")
    factory(recipeId, input, output, secondary, bonus)
  }

  override def fromNetwork(recipeId: ResourceLocation, buff: FriendlyByteBuf): T = {
    val input = Ingredient.fromNetwork(buff)
    val output = ItemStackWithChance.fromNetwork(buff)
    val secondary = ItemStackWithChance.fromNetwork(buff)
    val bonus = ItemStackWithChance.fromNetwork(buff)
    factory(recipeId, input, output, secondary, bonus)
  }

  override def toNetwork(buffer: FriendlyByteBuf, recipe: T): Unit = {
    recipe.input.toNetwork(buffer)
    recipe.output.toNetwork(buffer)
    recipe.secondary.toNetwork(buffer)
    recipe.bonus.toNetwork(buffer)
  }
}