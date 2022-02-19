package net.bdew.advtech.machines.alloy

import com.google.gson.JsonObject
import net.bdew.advtech.misc.IngredientMulti
import net.bdew.advtech.registries.Recipes
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.{RecipeSerializer, RecipeType, ShapedRecipe}

class AlloyRecipe(id: ResourceLocation,
                  val input1: IngredientMulti,
                  val input2: IngredientMulti,
                  val output: ItemStack,
                  val speedMod: Float,
                 ) extends BaseMachineRecipe(id) {
  override def getSerializer: RecipeSerializer[_] = Recipes.alloySerializer.get()
  override def getType: RecipeType[_] = Recipes.alloyType

  def test(stack1: ItemStack, stack2: ItemStack): Boolean =
    (input1.test(stack1) && input2.test(stack2)) || (input1.test(stack2) && input2.test(stack1))

  def testSoft(stack1: ItemStack, stack2: ItemStack): Boolean = (
    (input1.testSoft(stack1) && (input2.testSoft(stack2) || stack2.isEmpty))
      || (input1.testSoft(stack2) && (input2.testSoft(stack1) || stack1.isEmpty))
      || (input2.testSoft(stack1) && (input1.testSoft(stack2) || stack2.isEmpty))
      || (input2.testSoft(stack2) && (input1.testSoft(stack1) || stack1.isEmpty))
    )
}

class AlloyRecipeSerializer extends BaseMachineRecipeSerializer[AlloyRecipe] {
  override def fromJson(recipeId: ResourceLocation, obj: JsonObject): AlloyRecipe = {
    val input1 = IngredientMulti.fromJson(obj.getAsJsonObject("input1"))
    val input2 = IngredientMulti.fromJson(obj.getAsJsonObject("input2"))
    val output = ShapedRecipe.itemStackFromJson(obj.getAsJsonObject("output"))
    val speedMod = if (obj.has("speed")) obj.get("speed").getAsFloat else 1
    new AlloyRecipe(recipeId, input1, input2, output, speedMod)
  }

  override def fromNetwork(recipeId: ResourceLocation, buff: FriendlyByteBuf): AlloyRecipe = {
    val input1 = IngredientMulti.fromNetwork(buff)
    val input2 = IngredientMulti.fromNetwork(buff)
    val output = buff.readItem()
    val speedMod = buff.readFloat()
    new AlloyRecipe(recipeId, input1, input2, output, speedMod)
  }

  override def toNetwork(buff: FriendlyByteBuf, recipe: AlloyRecipe): Unit = {
    recipe.input1.toNetwork(buff)
    recipe.input2.toNetwork(buff)
    buff.writeItem(recipe.output)
    buff.writeFloat(recipe.speedMod)
  }
}