package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.registries.{MetalEntry, MetalItemType, Metals, Recipes}
import net.minecraft.data.DataGenerator
import net.minecraft.data.recipes.{FinishedRecipe, RecipeProvider, ShapelessRecipeBuilder, SimpleCookingRecipeBuilder}
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{ItemTags, Tag}
import net.minecraft.world.item.crafting.{Ingredient, RecipeSerializer}
import net.minecraft.world.item.{Item, Items}

import java.util.function.Consumer

class RecipeGen(gen: DataGenerator) extends RecipeProvider(gen) {
  def forgeTagCustom(name: String*): Tag.Named[Item] =
    ItemTags.createOptional(new ResourceLocation("forge", name.mkString("/")))


  def addStorageRecipes(metal: MetalEntry, big: MetalItemType, small: MetalItemType, consumer: Consumer[FinishedRecipe]): Unit = {
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

  override def buildCraftingRecipes(consumer: Consumer[FinishedRecipe]): Unit = {
    for (metal <- Metals.all) {
      if (metal.registerOre)
        addStorageRecipes(metal, MetalItemType.RawBlock, MetalItemType.RawDrop, consumer)

      if (metal.registerNugget && metal.registerIngot)
        addStorageRecipes(metal, MetalItemType.Ingot, MetalItemType.Nugget, consumer)

      if (metal.registerIngot && metal.registerBlock)
        addStorageRecipes(metal, MetalItemType.StorageBlock, MetalItemType.Ingot, consumer)

      if (metal.registerIngot && (metal.registerOre || metal.registerProcessing)) {
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

      if (metal.registerProcessing) {
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
    }
  }

  def makeCrusherRecipe(id: String, input: Tag.Named[Item], output: Item, gravel: Boolean, consumer: Consumer[FinishedRecipe]): Unit = {
    var builder = ProcessingRecipeBuilder(Recipes.crusherSerializer.get())
      .withInput(Ingredient.of(input))
      .withOutput(output, count = 2)
      .requireTag(input)

    if (gravel) builder = builder.withSecondary(Items.GRAVEL, chance = 0.1f)

    builder.build(id)
      .save(consumer)
  }
}
