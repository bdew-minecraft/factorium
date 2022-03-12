package net.bdew.factorium.datagen

import com.google.gson.{JsonArray, JsonObject}
import net.bdew.factorium.Factorium
import net.bdew.factorium.machines.processing.ProcessingRecipe
import net.bdew.factorium.misc.ItemStackWithChance
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.{Ingredient, RecipeSerializer}
import net.minecraft.world.item.{Item, ItemStack}
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.common.crafting.conditions.{ICondition, NotCondition, TagEmptyCondition}

import java.util.function.Consumer

case class ProcessingRecipeBuilder(serializer: RecipeSerializer[_ <: ProcessingRecipe],
                                   input: Ingredient = null,
                                   output: ItemStackWithChance = ItemStackWithChance.EMPTY,
                                   secondary: ItemStackWithChance = ItemStackWithChance.EMPTY,
                                   bonus: ItemStackWithChance = ItemStackWithChance.EMPTY,
                                   conditions: List[ICondition] = List.empty) {

  def withInput(input: Ingredient): ProcessingRecipeBuilder =
    copy(input = input)

  def withOutput(item: Item, count: Int = 1, chance: Float = 1): ProcessingRecipeBuilder =
    copy(output = ItemStackWithChance(new ItemStack(item, count), chance))

  def withSecondary(item: Item, count: Int = 1, chance: Float = 1): ProcessingRecipeBuilder =
    copy(secondary = ItemStackWithChance(new ItemStack(item, count), chance))

  def withBonus(item: Item, count: Int = 1, chance: Float = 1): ProcessingRecipeBuilder =
    copy(bonus = ItemStackWithChance(new ItemStack(item, count), chance))

  def requireCondition(condition: ICondition): ProcessingRecipeBuilder =
    copy(conditions = conditions :+ condition)

  def requireTag(tag: TagKey[Item]): ProcessingRecipeBuilder =
    requireCondition(new NotCondition(new TagEmptyCondition(tag.location)))

  def build(id: String): FinishedProcessingRecipe = {
    require(input != null, s"Input is empty in recipe $id")
    require(output.nonEmpty, s"Output is empty in recipe $id")
    new FinishedProcessingRecipe(id)
  }

  class FinishedProcessingRecipe(id: String) extends FinishedRecipe {
    override def getId: ResourceLocation = new ResourceLocation(Factorium.ModId, id)
    override def getType: RecipeSerializer[_] = serializer
    override def serializeAdvancement(): JsonObject = null
    override def getAdvancementId: ResourceLocation = null
    override def serializeRecipeData(obj: JsonObject): Unit = {
      if (conditions.nonEmpty) {
        val conditionsJson = new JsonArray()
        conditions.foreach(c => conditionsJson.add(CraftingHelper.serialize(c)))
        obj.add("conditions", conditionsJson)
      }
      obj.add("input", input.toJson)
      obj.add("output", RecipeHelper.writeItemStackWithChance(output))
      if (secondary.nonEmpty)
        obj.add("secondary", RecipeHelper.writeItemStackWithChance(secondary))
      if (bonus.nonEmpty) {
        obj.add("bonus", RecipeHelper.writeItemStackWithChance(bonus))
      }
    }

    def save(consumer: Consumer[FinishedRecipe]): Unit = consumer.accept(this)
  }
}
