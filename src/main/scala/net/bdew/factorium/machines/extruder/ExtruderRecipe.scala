package net.bdew.factorium.machines.extruder

import com.google.gson.JsonObject
import net.bdew.factorium.misc.IngredientMulti
import net.bdew.factorium.registries.Recipes
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.{Ingredient, RecipeSerializer, RecipeType, ShapedRecipe}

class ExtruderRecipe(id: ResourceLocation,
                     val input: IngredientMulti,
                     val die: Ingredient,
                     val output: ItemStack,
                    ) extends BaseMachineRecipe(id) {
  override def getSerializer: RecipeSerializer[_] = Recipes.extruderSerializer.get()

  override def getType: RecipeType[_] = Recipes.extruderType

  def test(inputStack: ItemStack, dieStack: ItemStack): Boolean =
    input.test(inputStack) && die.test(dieStack)

  def testSoft(inputStack: ItemStack, dieStack: ItemStack): Boolean =
    (input.test(inputStack) && (die.test(dieStack) || dieStack.isEmpty)
      || die.test(dieStack) && (input.test(inputStack) || inputStack.isEmpty))
}

class ExtruderRecipeSerializer extends BaseMachineRecipeSerializer[ExtruderRecipe] {
  override def fromJson(recipeId: ResourceLocation, obj: JsonObject): ExtruderRecipe = {
    val input = IngredientMulti.fromJson(obj.getAsJsonObject("input"))
    val die = Ingredient.fromJson(obj.getAsJsonObject("die"))
    val output = ShapedRecipe.itemStackFromJson(obj.getAsJsonObject("output"))
    new ExtruderRecipe(recipeId, input, die, output)
  }

  override def fromNetwork(recipeId: ResourceLocation, buff: FriendlyByteBuf): ExtruderRecipe = {
    val input = IngredientMulti.fromNetwork(buff)
    val die = Ingredient.fromNetwork(buff)
    val output = buff.readItem()
    new ExtruderRecipe(recipeId, input, die, output)
  }

  override def toNetwork(buff: FriendlyByteBuf, recipe: ExtruderRecipe): Unit = {
    recipe.input.toNetwork(buff)
    recipe.die.toNetwork(buff)
    buff.writeItem(recipe.output)
  }
}