package net.bdew.advtech.datagen

import com.google.gson.JsonObject
import net.bdew.advtech.machines.processing.ProcessingRecipe
import net.bdew.advtech.misc.ItemStackWithChance
import net.bdew.lib.recipes.BaseMachineRecipe
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraftforge.common.crafting.ConditionalRecipe
import net.minecraftforge.common.crafting.conditions.ICondition

import java.util.function.Consumer

object RecipeHelper {
  abstract class FinishedMachineRecipe(rec: BaseMachineRecipe) extends FinishedRecipe {
    override def getId: ResourceLocation = rec.getId
    override def getType: RecipeSerializer[_] = rec.getSerializer
    override def serializeAdvancement(): JsonObject = null
    override def getAdvancementId: ResourceLocation = null
  }

  class FinishedProcessingRecipe(rec: ProcessingRecipe) extends FinishedMachineRecipe(rec) {
    override def serializeRecipeData(obj: JsonObject): Unit = {
      obj.add("input", rec.input.toJson)
      obj.add("output", writeItemStackWithChance(rec.output))
      if (!rec.secondary.stack.isEmpty && rec.secondary.chance > 0)
        obj.add("secondary", writeItemStackWithChance(rec.secondary))
      if (!rec.bonus.stack.isEmpty && rec.bonus.chance > 0)
        obj.add("bonus", writeItemStackWithChance(rec.bonus))
    }
  }

  def writeItemStackWithChance(v: ItemStackWithChance): JsonObject = {
    val obj = new JsonObject
    obj.addProperty("item", v.stack.getItem.getRegistryName.toString)
    obj.addProperty("count", v.stack.getCount)
    obj.addProperty("chance", v.chance)
    obj
  }

  def saveProcessingRecipe(recipe: ProcessingRecipe, consumer: Consumer[FinishedRecipe], conditions: ICondition*): Unit = {
    if (conditions.isEmpty) {
      consumer.accept(new FinishedProcessingRecipe(recipe))
    } else {
      val res = conditions.foldRight(ConditionalRecipe.builder())((c, r) => r.addCondition(c))
      res.addRecipe(new FinishedProcessingRecipe(recipe)).build(consumer, recipe.id)
    }
  }
}
