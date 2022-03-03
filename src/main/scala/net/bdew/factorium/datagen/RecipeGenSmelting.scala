package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.metals.{MetalEntry, MetalItemType}
import net.minecraft.data.recipes.{FinishedRecipe, RecipeBuilder, SimpleCookingRecipeBuilder}
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.crafting.{Ingredient, RecipeSerializer}

import java.util.function.Consumer

object RecipeGenSmelting {
  def addSmeltingRecipes(metal: MetalEntry, consumer: Consumer[FinishedRecipe]): Unit = {
    val smeltableTag = ItemTags.createOptional(new ResourceLocation(Factorium.ModId, s"smeltable/${metal.name}"))

    SimpleCookingRecipeBuilder.cooking(
      Ingredient.of(smeltableTag),
      metal.item(MetalItemType.Ingot),
      0.7f, 200,
      RecipeSerializer.SMELTING_RECIPE
    )
      .asInstanceOf[RecipeBuilder]
      .unlockedBy("has_item", RecipeHelper.has(smeltableTag))
      .group(s"${Factorium.ModId}:smelting")
      .save(consumer, new ResourceLocation(Factorium.ModId, s"metals/${metal.name}/ingot_from_smelting"))

    SimpleCookingRecipeBuilder.cooking(
      Ingredient.of(smeltableTag),
      metal.item(MetalItemType.Ingot),
      0.7f, 100,
      RecipeSerializer.BLASTING_RECIPE
    ).asInstanceOf[RecipeBuilder]
      .unlockedBy("has_item", RecipeHelper.has(smeltableTag))
      .group(s"${Factorium.ModId}:smelting")
      .save(consumer, new ResourceLocation(Factorium.ModId, s"metals/${metal.name}/ingot_from_blasting"))
  }
}
