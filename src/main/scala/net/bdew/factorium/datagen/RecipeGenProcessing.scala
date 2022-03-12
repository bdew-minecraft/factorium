package net.bdew.factorium.datagen

import net.bdew.factorium.metals.{MetalEntry, MetalItemType}
import net.bdew.factorium.registries.Recipes
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.{Item, Items}

import java.util.function.Consumer

object RecipeGenProcessing {
  def addProcessingRecipes(metal: MetalEntry, consumer: Consumer[FinishedRecipe]): Unit = {
    if (metal.ownItem(MetalItemType.Chunks)) {
      makeCrusherRecipe(
        id = s"metals/${metal.name}/crushing_ore",
        input = CustomTags.ores(metal.name).item,
        output = metal.item(MetalItemType.Chunks),
        gravel = true,
        consumer
      )
      makeCrusherRecipe(
        id = s"metals/${metal.name}/crushing_raw",
        input = CustomTags.rawMaterials(metal.name),
        output = metal.item(MetalItemType.Chunks),
        gravel = false,
        consumer
      )
    }

    if (metal.ownItem(MetalItemType.Powder)) {
      makeGrinderRecipe(
        id = s"metals/${metal.name}/grinding",
        input = CustomTags.chunks(metal.name),
        output = metal.item(MetalItemType.Powder),
        consumer
      )
    }

    if (metal.ownItem(MetalItemType.Dust)) {
      makePulverizerRecipe(
        id = s"metals/${metal.name}/pulverizing",
        input = CustomTags.powders(metal.name),
        output = metal.item(MetalItemType.Dust),
        consumer
      )
    }
  }

  def makeCrusherRecipe(id: String, input: TagKey[Item], output: Item, gravel: Boolean, consumer: Consumer[FinishedRecipe]): Unit = {
    var builder = ProcessingRecipeBuilder(Recipes.crusher.serializer)
      .withInput(Ingredient.of(input))
      .withOutput(output, count = 2)
      .requireTag(input)

    if (gravel) builder = builder.withSecondary(Items.GRAVEL, chance = 0.1f)

    builder.build(id).save(consumer)
  }

  def makeGrinderRecipe(id: String, input: TagKey[Item], output: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    ProcessingRecipeBuilder(Recipes.grinder.serializer)
      .withInput(Ingredient.of(input))
      .withOutput(output)
      .withBonus(output, chance = 0.25f)
      .requireTag(input)
      .build(id)
      .save(consumer)
  }

  def makePulverizerRecipe(id: String, input: TagKey[Item], output: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    ProcessingRecipeBuilder(Recipes.pulverizer.serializer)
      .withInput(Ingredient.of(input))
      .withOutput(output)
      .withBonus(output, chance = 0.25f)
      .requireTag(input)
      .build(id)
      .save(consumer)
  }
}
