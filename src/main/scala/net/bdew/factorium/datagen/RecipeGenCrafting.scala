package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.metals.{MetalEntry, MetalItemType}
import net.minecraft.data.recipes.{FinishedRecipe, RecipeBuilder, ShapedRecipeBuilder, ShapelessRecipeBuilder}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

import java.util.function.Consumer

object RecipeGenCrafting {
  def addCraftingRecipes(metal: MetalEntry, consumer: Consumer[FinishedRecipe]): Unit = {
    if (metal.ownItem(MetalItemType.Plate)) {
      val ingotTag = CustomTags.ingots(metal.name)
      ShapedRecipeBuilder.shaped(metal.item(MetalItemType.Plate), 3)
        .define('x', Ingredient.of(ingotTag))
        .pattern("xxx")
        .asInstanceOf[RecipeBuilder]
        .unlockedBy("has_item", RecipeHelper.has(ingotTag))
        .group(s"${Factorium.ModId}:plates")
        .save(consumer, new ResourceLocation(Factorium.ModId, s"metals/${metal.name}/plate"))
    }

    if (metal.ownItem(MetalItemType.Gear)) {
      val ingotTag = CustomTags.ingots(metal.name)
      ShapedRecipeBuilder.shaped(metal.item(MetalItemType.Gear))
        .define('x', Ingredient.of(ingotTag))
        .pattern(" x ")
        .pattern("xxx")
        .pattern(" x ")
        .asInstanceOf[RecipeBuilder]
        .unlockedBy("has_item", RecipeHelper.has(ingotTag))
        .group(s"${Factorium.ModId}:gears")
        .save(consumer, new ResourceLocation(Factorium.ModId, s"metals/${metal.name}/gear"))
    }

    if (metal.ownItem(MetalItemType.Wire)) {
      val nuggetTag = CustomTags.nuggets(metal.name)
      ShapedRecipeBuilder.shaped(metal.item(MetalItemType.Wire))
        .define('x', Ingredient.of(nuggetTag))
        .pattern("xxx")
        .pattern("x  ")
        .pattern("xxx")
        .asInstanceOf[RecipeBuilder]
        .unlockedBy("has_item", RecipeHelper.has(nuggetTag))
        .group(s"${Factorium.ModId}:wires")
        .save(consumer, new ResourceLocation(Factorium.ModId, s"metals/${metal.name}/wire"))
    }

    if (metal.ownItem(MetalItemType.Rod)) {
      val ingotTag = CustomTags.ingots(metal.name)
      ShapedRecipeBuilder.shaped(metal.item(MetalItemType.Rod), 4)
        .define('x', Ingredient.of(ingotTag))
        .pattern(" x ")
        .pattern(" x ")
        .asInstanceOf[RecipeBuilder]
        .unlockedBy("has_item", RecipeHelper.has(ingotTag))
        .group(s"${Factorium.ModId}:rods")
        .save(consumer, new ResourceLocation(Factorium.ModId, s"metals/${metal.name}/rod"))
    }

    maybeAddStorageRecipes(metal, MetalItemType.RawBlock, MetalItemType.RawDrop, consumer)
    maybeAddStorageRecipes(metal, MetalItemType.Ingot, MetalItemType.Nugget, consumer)
    maybeAddStorageRecipes(metal, MetalItemType.StorageBlock, MetalItemType.Ingot, consumer)
  }

  def maybeAddStorageRecipes(metal: MetalEntry, big: MetalItemType, small: MetalItemType, consumer: Consumer[FinishedRecipe]): Unit = {
    if (metal.ownItem(big) || metal.ownItem(small)) {
      ShapelessRecipeBuilder.shapeless(metal.item(small), 9)
        .requires(metal.item(big))
        .asInstanceOf[RecipeBuilder]
        .unlockedBy("has_item", RecipeHelper.has(metal.item(big)))
        .group(s"${Factorium.ModId}:misc")
        .save(consumer, new ResourceLocation(Factorium.ModId, s"metals/${metal.name}/${small.kind}_from_${big.kind}"))
      ShapelessRecipeBuilder.shapeless(metal.item(big))
        .requires(metal.item(small), 9)
        .asInstanceOf[RecipeBuilder]
        .unlockedBy("has_item", RecipeHelper.has(metal.item(small)))
        .group(s"${Factorium.ModId}:misc")
        .save(consumer, new ResourceLocation(Factorium.ModId, s"metals/${metal.name}/${big.kind}_from_${small.kind}"))
    }
  }
}
