package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.metals.{MetalEntry, MetalItemType, Metals}
import net.bdew.advtech.registries.Recipes
import net.minecraft.data.DataGenerator
import net.minecraft.data.recipes._
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{ItemTags, Tag}
import net.minecraft.world.item.crafting.{Ingredient, RecipeSerializer}
import net.minecraft.world.item.{Item, Items}

import java.util.function.Consumer

class RecipeGen(gen: DataGenerator) extends RecipeProvider(gen) {
  def forgeTagCustom(name: String*): Tag.Named[Item] =
    ItemTags.createOptional(new ResourceLocation("forge", name.mkString("/")))

  def myTag(name: String*): Tag.Named[Item] =
    ItemTags.createOptional(new ResourceLocation(AdvTech.ModId, name.mkString("/")))

  override def buildCraftingRecipes(consumer: Consumer[FinishedRecipe]): Unit = {
    for (metal <- Metals.all) {
      maybeAddStorageRecipes(metal, MetalItemType.RawBlock, MetalItemType.RawDrop, consumer)
      maybeAddStorageRecipes(metal, MetalItemType.Ingot, MetalItemType.Nugget, consumer)
      maybeAddStorageRecipes(metal, MetalItemType.StorageBlock, MetalItemType.Ingot, consumer)

      if (metal.haveItem(MetalItemType.Ingot) && MetalItemType.smeltables.exists(metal.ownItem)) {
        val smeltableTag = ItemTags.createOptional(new ResourceLocation(AdvTech.ModId, s"smeltable/${metal.name}"))

        SimpleCookingRecipeBuilder.cooking(
          Ingredient.of(smeltableTag),
          metal.item(MetalItemType.Ingot),
          0.7f, 200,
          RecipeSerializer.SMELTING_RECIPE
        )
          .unlockedBy("has_item", RecipeProvider.has(smeltableTag))
          .group(s"${AdvTech.ModId}:smelting")
          .save(consumer, new ResourceLocation(AdvTech.ModId, s"metals/${metal.name}/ingot_from_smelting"))

        SimpleCookingRecipeBuilder.cooking(
          Ingredient.of(smeltableTag),
          metal.item(MetalItemType.Ingot),
          0.7f, 100,
          RecipeSerializer.BLASTING_RECIPE
        )
          .unlockedBy("has_item", RecipeProvider.has(smeltableTag))
          .group(s"${AdvTech.ModId}:smelting")
          .save(consumer, new ResourceLocation(AdvTech.ModId, s"metals/${metal.name}/ingot_from_blasting"))
      }

      if (metal.ownItem(MetalItemType.Plate)) {
        val ingotTag = forgeTagCustom("ingots", metal.name)
        ShapedRecipeBuilder.shaped(metal.item(MetalItemType.Plate), 3)
          .define('x', Ingredient.of(ingotTag))
          .pattern("xxx")
          .unlockedBy("has_item", RecipeProvider.has(ingotTag))
          .group(s"${AdvTech.ModId}:plates")
          .save(consumer, new ResourceLocation(AdvTech.ModId, s"metals/${metal.name}/plate"))
      }

      if (metal.ownItem(MetalItemType.Gear)) {
        val ingotTag = forgeTagCustom("ingots", metal.name)
        ShapedRecipeBuilder.shaped(metal.item(MetalItemType.Gear))
          .define('x', Ingredient.of(ingotTag))
          .pattern(" x ")
          .pattern("xxx")
          .pattern(" x ")
          .unlockedBy("has_item", RecipeProvider.has(ingotTag))
          .group(s"${AdvTech.ModId}:gears")
          .save(consumer, new ResourceLocation(AdvTech.ModId, s"metals/${metal.name}/gear"))
      }

      if (metal.ownItem(MetalItemType.Rod)) {
        val ingotTag = forgeTagCustom("ingots", metal.name)
        ShapedRecipeBuilder.shaped(metal.item(MetalItemType.Rod), 4)
          .define('x', Ingredient.of(ingotTag))
          .pattern(" x ")
          .pattern(" x ")
          .unlockedBy("has_item", RecipeProvider.has(ingotTag))
          .group(s"${AdvTech.ModId}:rods")
          .save(consumer, new ResourceLocation(AdvTech.ModId, s"metals/${metal.name}/rod"))
      }

      if (metal.ownItem(MetalItemType.Chunks)) {
        makeCrusherRecipe(
          id = s"metals/${metal.name}/crushing_ore",
          input = forgeTagCustom("ores", metal.name),
          output = metal.item(MetalItemType.Chunks),
          gravel = true,
          consumer
        )
        makeCrusherRecipe(
          id = s"metals/${metal.name}/crushing_raw",
          input = forgeTagCustom("raw_materials", metal.name),
          output = metal.item(MetalItemType.Chunks),
          gravel = false,
          consumer
        )
      }

      if (metal.ownItem(MetalItemType.Powder)) {
        makeGrinderRecipe(
          id = s"metals/${metal.name}/grinding",
          input = myTag("chunks", metal.name),
          output = metal.item(MetalItemType.Powder),
          consumer
        )
      }

      if (metal.ownItem(MetalItemType.Dust)) {
        makePulverizerRecipe(
          id = s"metals/${metal.name}/pulverizing",
          input = myTag("powders", metal.name),
          output = metal.item(MetalItemType.Dust),
          consumer
        )
      }
    }
  }

  def maybeAddStorageRecipes(metal: MetalEntry, big: MetalItemType, small: MetalItemType, consumer: Consumer[FinishedRecipe]): Unit = {
    if (metal.ownItem(big) || metal.ownItem(small)) {
      ShapelessRecipeBuilder.shapeless(metal.item(small), 9)
        .requires(metal.item(big))
        .unlockedBy("has_item", RecipeProvider.has(metal.item(big)))
        .group(s"${AdvTech.ModId}:misc")
        .save(consumer, new ResourceLocation(AdvTech.ModId, s"metals/${metal.name}/${small.kind}_from_${big.kind}"))
      ShapelessRecipeBuilder.shapeless(metal.item(big))
        .requires(metal.item(small), 9)
        .unlockedBy("has_item", RecipeProvider.has(metal.item(small)))
        .group(s"${AdvTech.ModId}:misc")
        .save(consumer, new ResourceLocation(AdvTech.ModId, s"metals/${metal.name}/${big.kind}_from_${small.kind}"))
    }
  }

  def makeCrusherRecipe(id: String, input: Tag.Named[Item], output: Item, gravel: Boolean, consumer: Consumer[FinishedRecipe]): Unit = {
    var builder = ProcessingRecipeBuilder(Recipes.crusherSerializer.get())
      .withInput(Ingredient.of(input))
      .withOutput(output, count = 2)
      .requireTag(input)

    if (gravel) builder = builder.withSecondary(Items.GRAVEL, chance = 0.1f)

    builder.build(id).save(consumer)
  }

  def makeGrinderRecipe(id: String, input: Tag.Named[Item], output: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    ProcessingRecipeBuilder(Recipes.grinderSerializer.get())
      .withInput(Ingredient.of(input))
      .withOutput(output)
      .withBonus(output, chance = 0.25f)
      .requireTag(input)
      .build(id)
      .save(consumer)
  }

  def makePulverizerRecipe(id: String, input: Tag.Named[Item], output: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    ProcessingRecipeBuilder(Recipes.pulverizerSerializer.get())
      .withInput(Ingredient.of(input))
      .withOutput(output)
      .withBonus(output, chance = 0.25f)
      .requireTag(input)
      .build(id)
      .save(consumer)
  }
}
