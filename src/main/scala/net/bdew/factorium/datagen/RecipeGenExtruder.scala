package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.metals.{MetalEntry, MetalItemType}
import net.bdew.factorium.registries.Items
import net.minecraft.data.recipes.{FinishedRecipe, RecipeBuilder, RecipeCategory, ShapelessRecipeBuilder}
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient

import java.util.function.Consumer

object RecipeGenExtruder {
  def addExtruderRecipes(metal: MetalEntry, consumer: Consumer[FinishedRecipe]): Unit = {
    makeExtruderRecipe(metal, output = MetalItemType.Rod, inputCount = 1, outputCount = 2, consumer)
    makeExtruderRecipe(metal, output = MetalItemType.Plate, inputCount = 1, outputCount = 1, consumer)
    makeExtruderRecipe(metal, output = MetalItemType.Wire, inputCount = 1, outputCount = 2, consumer)
    makeExtruderRecipe(metal, output = MetalItemType.Gear, inputCount = 4, outputCount = 1, consumer)
    makeExtruderRecipe(metal, output = MetalItemType.Nugget, inputCount = 1, outputCount = 9, consumer)
  }

  def makeExtruderRecipe(metal: MetalEntry, output: MetalItemType, inputCount: Int, outputCount: Int, consumer: Consumer[FinishedRecipe]): Unit = {
    if (!metal.haveItem(output)) return
    val ingotTag = CustomTags.ingots(metal.name)
    ExtruderRecipeBuilder()
      .withInput(Ingredient.of(ingotTag), inputCount)
      .withDie(Ingredient.of(Items.dies(output.kind).get()))
      .withOutput(metal.item(output), outputCount)
      .build(s"metals/${metal.name}/${output.kind}_from_extruder")
      .save(consumer)
  }

  def makeDieRecipe(dieItem: String, itemType: MetalItemType, inputTag: TagKey[Item], consumer: Consumer[FinishedRecipe]): Unit = {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.dies(dieItem).get(), 1)
      .requires(Ingredient.of(inputTag))
      .requires(Ingredient.of(CustomTags.plates("steel")))
      .asInstanceOf[RecipeBuilder]
      .unlockedBy("has_item", RecipeHelper.has(inputTag))
      .unlockedBy("has_plate", RecipeHelper.has(CustomTags.plates("steel")))
      .group(s"${Factorium.ModId}:dies")
      .save(consumer, new ResourceLocation(Factorium.ModId, s"dies/${itemType.kind}"))
  }
}
