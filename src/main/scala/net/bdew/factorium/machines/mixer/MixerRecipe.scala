package net.bdew.factorium.machines.mixer

import com.google.gson.JsonObject
import net.bdew.factorium.misc.IngredientMulti
import net.bdew.factorium.registries.Recipes
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer, FluidStackIngredient}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.{RecipeSerializer, RecipeType, ShapedRecipe}
import net.minecraftforge.fluids.FluidStack

class MixerRecipe(id: ResourceLocation,
                  val inputItem: IngredientMulti,
                  val inputFluid: FluidStackIngredient,
                  val outputItem: ItemStack,
                  val outputFluid: FluidStackIngredient,
                 ) extends BaseMachineRecipe(id) {

  def test(item: ItemStack, fluid: FluidStack): Boolean =
    inputItem.test(item) && inputFluid.test(fluid)

  def testSoft(item: ItemStack, fluid: FluidStack): Boolean =
    (item.isEmpty || inputItem.testSoft(item)) && (fluid.isEmpty || inputFluid.test(fluid.getFluid))

  override def getSerializer: RecipeSerializer[_] = Recipes.mixer.serializer
  override def getType: RecipeType[_] = Recipes.mixer.recipeType
}

class MixerRecipeSerializer extends BaseMachineRecipeSerializer[MixerRecipe] {
  override def fromJson(recipeId: ResourceLocation, obj: JsonObject): MixerRecipe = {
    val inputItem = IngredientMulti.fromJson(obj.getAsJsonObject("inputItem"))
    val inputFluid = FluidStackIngredient.fromJson(obj.getAsJsonObject("inputFluid"))

    val outputItem =
      if (obj.has("outputItem"))
        ShapedRecipe.itemStackFromJson(obj.getAsJsonObject("outputItem"))
      else
        ItemStack.EMPTY

    val outputFluid =
      if (obj.has("outputFluid"))
        FluidStackIngredient.fromJson(obj.getAsJsonObject("outputFluid"))
      else
        FluidStackIngredient.EMPTY

    new MixerRecipe(recipeId, inputItem, inputFluid, outputItem, outputFluid)
  }

  override def fromNetwork(recipeId: ResourceLocation, buff: FriendlyByteBuf): MixerRecipe = {
    val inputItem = IngredientMulti.fromNetwork(buff)
    val inputFluid = FluidStackIngredient.fromPacket(buff)
    val outputItem = buff.readItem()
    val outputFluid = FluidStackIngredient.fromPacket(buff)
    new MixerRecipe(recipeId, inputItem, inputFluid, outputItem, outputFluid)
  }

  override def toNetwork(buff: FriendlyByteBuf, recipe: MixerRecipe): Unit = {
    recipe.inputItem.toNetwork(buff)
    recipe.inputFluid.toPacket(buff)
    buff.writeItem(recipe.outputItem)
    recipe.outputFluid.toPacket(buff)
  }
}