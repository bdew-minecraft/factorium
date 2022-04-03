package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.registries.{Items, Recipes}
import net.minecraft.data.recipes.{FinishedRecipe, RecipeBuilder, ShapelessRecipeBuilder}
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.FluidTags
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.{DyeColor, Item, Items => MCItems}
import net.minecraftforge.common.Tags

import java.util.Locale
import java.util.function.Consumer

object RecipeGenColored {
  def makeColoredRecipes(consumer: Consumer[FinishedRecipe]): Unit = {
    DyeColor.values().foreach(x => makeColoredRecipesFor(x.name().toLowerCase(Locale.US), consumer))
  }

  def makeColoredRecipesFor(color: String, consumer: Consumer[FinishedRecipe]): Unit = {

    makeDyeRecipe(color, consumer)

    val concretePowder = RecipeHelper.mcItem(s"${color}_concrete_powder")
    makeConcretePowderRecipe(color, concretePowder, consumer)
    makeConcreteMixerRecipe(concretePowder, RecipeHelper.mcItem(s"${color}_concrete"), consumer)

    makeWoolToStringRecipe(color, consumer)
  }

  def makeDyeRecipe(color: String, consumer: Consumer[FinishedRecipe]): Unit = {
    val dye = RecipeHelper.mcItem(s"${color}_dye")
    ProcessingRecipeBuilder(Recipes.pulverizer.serializer)
      .withInput(Ingredient.of(CustomTags.flowers(color)))
      .withOutput(dye, 4)
      .build(s"dyes/$color")
      .save(consumer)
  }

  def makeConcretePowderRecipe(color: String, powder: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    ShapelessRecipeBuilder.shapeless(powder, 16)
      .requires(CustomTags.dyes(color))
      .requires(Ingredient.of(Tags.Items.GRAVEL), 3)
      .requires(Ingredient.of(Tags.Items.SAND), 3)
      .requires(Items.extraDusts("quicklime").get())
      .requires(MCItems.CLAY_BALL)
      .asInstanceOf[RecipeBuilder]
      .unlockedBy("has_clay", RecipeHelper.has(MCItems.CLAY_BALL))
      .unlockedBy("has_sand", RecipeHelper.has(Tags.Items.SAND))
      .unlockedBy("has_gravel", RecipeHelper.has(Tags.Items.GRAVEL))
      .unlockedBy("has_quicklime", RecipeHelper.has(CustomTags.dusts("quicklime")))
      .group("concrete_powder")
      .save(consumer, new ResourceLocation(Factorium.ModId, s"concrete/powder/${powder.getRegistryName.getPath}"))
  }

  def makeConcreteMixerRecipe(powder: Item, block: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    MixerRecipeBuilder()
      .withInputItem(Ingredient.of(powder))
      .withInputFluid(FluidTags.WATER, 250)
      .withOutputItem(block)
      .build(s"concrete/mixer/${block.getRegistryName.getPath}")
      .save(consumer)
  }

  def makeWoolToStringRecipe(color: String, consumer: Consumer[FinishedRecipe]): Unit = {
    val input = RecipeHelper.mcItem(s"${color}_wool")
    val dye = RecipeHelper.mcItem(s"${color}_dye")

    var recipe = ProcessingRecipeBuilder(Recipes.crusher.serializer)
      .withInput(Ingredient.of(input))
      .withOutput(MCItems.STRING, 4)

    if (color != "white") recipe = recipe.withBonus(dye, chance = 0.1f)

    recipe.build(s"wool/crush_${color}_wool")
      .save(consumer)
  }
}
