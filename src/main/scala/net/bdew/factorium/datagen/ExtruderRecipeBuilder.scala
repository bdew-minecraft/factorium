package net.bdew.factorium.datagen

import com.google.gson.{JsonArray, JsonObject}
import net.bdew.factorium.Factorium
import net.bdew.factorium.misc.IngredientMulti
import net.bdew.factorium.registries.Recipes
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.Tag
import net.minecraft.world.item.crafting.{Ingredient, RecipeSerializer}
import net.minecraft.world.item.{Item, ItemStack}
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.common.crafting.conditions.{ICondition, NotCondition, TagEmptyCondition}

import java.util.function.Consumer

case class ExtruderRecipeBuilder(input: IngredientMulti = null,
                                 die: Ingredient = null,
                                 output: ItemStack = ItemStack.EMPTY,
                                 conditions: List[ICondition] = List.empty) {

  def withInput(input: IngredientMulti): ExtruderRecipeBuilder =
    copy(input = input)

  def withInput(input: Ingredient, count: Int): ExtruderRecipeBuilder =
    copy(input = IngredientMulti(input, count))

  def withDie(die: Ingredient): ExtruderRecipeBuilder =
    copy(die = die)

  def withOutput(item: Item, count: Int = 1): ExtruderRecipeBuilder =
    copy(output = new ItemStack(item, count))

  def requireCondition(condition: ICondition): ExtruderRecipeBuilder =
    copy(conditions = conditions :+ condition)

  def requireTag(tag: Tag.Named[Item]): ExtruderRecipeBuilder =
    requireCondition(new NotCondition(new TagEmptyCondition(tag.getName)))

  def build(id: String): FinishedExtruderRecipe = {
    require(input != null, s"Input is empty in recipe $id")
    require(die != null, s"Die is empty in recipe $id")
    require(!output.isEmpty, s"Output is empty in recipe $id")
    new FinishedExtruderRecipe(id)
  }

  class FinishedExtruderRecipe(id: String) extends FinishedRecipe {
    override def getId: ResourceLocation = new ResourceLocation(Factorium.ModId, id)

    override def getType: RecipeSerializer[_] = Recipes.extruderSerializer.get()

    override def serializeAdvancement(): JsonObject = null

    override def getAdvancementId: ResourceLocation = null

    override def serializeRecipeData(obj: JsonObject): Unit = {
      if (conditions.nonEmpty) {
        val conditionsJson = new JsonArray()
        conditions.foreach(c => conditionsJson.add(CraftingHelper.serialize(c)))
        obj.add("conditions", conditionsJson)
      }
      obj.add("input", input.toJson)
      obj.add("die", die.toJson)
      obj.add("output", RecipeHelper.writeItemStack(output))
    }

    def save(consumer: Consumer[FinishedRecipe]): Unit = consumer.accept(this)
  }
}
