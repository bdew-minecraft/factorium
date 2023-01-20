package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.metals.{MetalEntry, MetalItemType}
import net.minecraft.data.recipes.{FinishedRecipe, RecipeBuilder, RecipeCategory, SimpleCookingRecipeBuilder}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

import java.util.function.Consumer

object RecipeGenSmelting {
  def addSmeltingRecipes(metal: MetalEntry, consumer: Consumer[FinishedRecipe]): Unit = {
    val smeltableTag = CustomTags.smeltable(metal.name)

    SimpleCookingRecipeBuilder.smelting(
      Ingredient.of(smeltableTag),
      RecipeCategory.MISC,
      metal.item(MetalItemType.Ingot),
      0.7f, 200
    )
      .asInstanceOf[RecipeBuilder]
      .unlockedBy("has_item", RecipeHelper.has(smeltableTag))
      .group(s"${Factorium.ModId}:smelting")
      .save(consumer, new ResourceLocation(Factorium.ModId, s"metals/${metal.name}/ingot_from_smelting"))

    SimpleCookingRecipeBuilder.blasting(
      Ingredient.of(smeltableTag),
      RecipeCategory.MISC,
      metal.item(MetalItemType.Ingot),
      0.7f, 100
    ).asInstanceOf[RecipeBuilder]
      .unlockedBy("has_item", RecipeHelper.has(smeltableTag))
      .group(s"${Factorium.ModId}:smelting")
      .save(consumer, new ResourceLocation(Factorium.ModId, s"metals/${metal.name}/ingot_from_blasting"))
  }
}
