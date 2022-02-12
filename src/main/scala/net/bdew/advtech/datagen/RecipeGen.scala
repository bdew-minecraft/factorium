package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.machines.processing.crusher.CrusherRecipe
import net.bdew.advtech.misc.ItemStackWithChance
import net.bdew.advtech.registries.{MetalEntry, MetalItemType, Metals}
import net.minecraft.data.DataGenerator
import net.minecraft.data.recipes.{FinishedRecipe, RecipeProvider, ShapelessRecipeBuilder, SimpleCookingRecipeBuilder}
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{ItemTags, Tag}
import net.minecraft.world.item.crafting.{Ingredient, RecipeSerializer}
import net.minecraft.world.item.{Item, ItemStack}
import net.minecraftforge.common.crafting.conditions.{NotCondition, TagEmptyCondition}

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

      val oreTag = forgeTagCustom("ores", metal.name)
      val rawTag = forgeTagCustom("raw_materials", metal.name)

      if (metal.registerProcessing) {
        RecipeHelper.saveProcessingRecipe(
          new CrusherRecipe(
            new ResourceLocation(AdvTech.ModId, s"metals/${metal.name}/crushing_ore"),
            Ingredient.of(oreTag),
            new ItemStackWithChance(new ItemStack(metal.item(MetalItemType.Chunks), 2), 1),
            new ItemStackWithChance(new ItemStack(metal.item(MetalItemType.Chunks)), 0.5f),
            ItemStackWithChance.EMPTY,
          ), consumer, new NotCondition(new TagEmptyCondition(oreTag.getName)))

        RecipeHelper.saveProcessingRecipe(
          new CrusherRecipe(
            new ResourceLocation(AdvTech.ModId, s"metals/${metal.name}/crushing_raw"),
            Ingredient.of(rawTag),
            new ItemStackWithChance(new ItemStack(metal.item(MetalItemType.Chunks), 2), 1),
            new ItemStackWithChance(new ItemStack(metal.item(MetalItemType.Chunks)), 0.5f),
            ItemStackWithChance.EMPTY,
          ), consumer, new NotCondition(new TagEmptyCondition(oreTag.getName)))
      }
    }
  }
}
