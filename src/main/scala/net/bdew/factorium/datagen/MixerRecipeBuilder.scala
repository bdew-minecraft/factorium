package net.bdew.factorium.datagen

import com.google.gson.{JsonArray, JsonObject}
import net.bdew.factorium.Factorium
import net.bdew.factorium.misc.IngredientMulti
import net.bdew.factorium.registries.Recipes
import net.bdew.lib.recipes.{FluidStackIngredient, GenIngredient}
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.{Ingredient, RecipeSerializer}
import net.minecraft.world.item.{Item, ItemStack}
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.common.crafting.conditions.{ICondition, NotCondition, TagEmptyCondition}

import java.util.function.Consumer

case class MixerRecipeBuilder(inputItem: IngredientMulti = null,
                              inputFluid: FluidStackIngredient = FluidStackIngredient.EMPTY,
                              outputItem: ItemStack = ItemStack.EMPTY,
                              outputFluid: FluidStackIngredient = FluidStackIngredient.EMPTY,
                              conditions: List[ICondition] = List.empty) {

  def withInputItem(input: IngredientMulti): MixerRecipeBuilder =
    copy(inputItem = input)

  def withInputItem(input: Ingredient, count: Int = 1): MixerRecipeBuilder =
    copy(inputItem = IngredientMulti(input, count))

  def withInputFluid(fluid: Fluid, amount: Int): MixerRecipeBuilder =
    copy(inputFluid = FluidStackIngredient(GenIngredient.of(fluid), amount))

  def withInputFluid(fluid: TagKey[Fluid], amount: Int): MixerRecipeBuilder =
    copy(inputFluid = FluidStackIngredient(GenIngredient.of(fluid), amount))

  def withOutputItem(input: Item, count: Int = 1): MixerRecipeBuilder =
    copy(outputItem = new ItemStack(input, count))

  def withOutputFluid(fluid: Fluid, amount: Int): MixerRecipeBuilder =
    copy(outputFluid = FluidStackIngredient(GenIngredient.of(fluid), amount))

  def requireCondition(condition: ICondition): MixerRecipeBuilder =
    copy(conditions = conditions :+ condition)

  def requireTag(tag: TagKey[Item]): MixerRecipeBuilder =
    requireCondition(new NotCondition(new TagEmptyCondition(tag.location)))

  def build(id: String): FinishedMixerRecipe = {
    require(inputItem != null, s"Input item is empty in recipe $id")
    require(!inputFluid.isEmpty, s"Input fluid is empty in recipe $id")
    require(!outputItem.isEmpty || !outputFluid.isEmpty, s"Output is empty in recipe $id")
    new FinishedMixerRecipe(id)
  }

  class FinishedMixerRecipe(id: String) extends FinishedRecipe {
    override def getId: ResourceLocation = new ResourceLocation(Factorium.ModId, id)
    override def getType: RecipeSerializer[_] = Recipes.mixer.serializer
    override def serializeAdvancement(): JsonObject = null
    override def getAdvancementId: ResourceLocation = null
    override def serializeRecipeData(obj: JsonObject): Unit = {
      if (conditions.nonEmpty) {
        val conditionsJson = new JsonArray()
        conditions.foreach(c => conditionsJson.add(CraftingHelper.serialize(c)))
        obj.add("conditions", conditionsJson)
      }

      obj.add("inputItem", inputItem.toJson)
      obj.add("inputFluid", RecipeHelper.writeFluidStackIngredient(inputFluid))

      if (!outputItem.isEmpty)
        obj.add("outputItem", RecipeHelper.writeItemStack(outputItem))

      if (!outputFluid.isEmpty)
        obj.add("outputFluid", RecipeHelper.writeFluidStackIngredient(outputFluid))
    }

    def save(consumer: Consumer[FinishedRecipe]): Unit = consumer.accept(this)
  }
}
