package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.registries.{Items, Recipes}
import net.minecraft.data.recipes.{FinishedRecipe, RecipeBuilder, ShapelessRecipeBuilder}
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.FluidTags
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.{Item, Items => MCItems}
import net.minecraftforge.common.Tags

import java.util.function.Consumer

object RecipeGenColored {
  def makeColoredRecipes(consumer: Consumer[FinishedRecipe]): Unit = {
    makeColoredRecipesFor("white", consumer)
    makeColoredRecipesFor("orange", consumer)
    makeColoredRecipesFor("magenta", consumer)
    makeColoredRecipesFor("light_blue", consumer)
    makeColoredRecipesFor("yellow", consumer)
    makeColoredRecipesFor("lime", consumer)
    makeColoredRecipesFor("pink", consumer)
    makeColoredRecipesFor("gray", consumer)
    makeColoredRecipesFor("light_gray", consumer)
    makeColoredRecipesFor("cyan", consumer)
    makeColoredRecipesFor("purple", consumer)
    makeColoredRecipesFor("blue", consumer)
    makeColoredRecipesFor("brown", consumer)
    makeColoredRecipesFor("green", consumer)
    makeColoredRecipesFor("red", consumer)
    makeColoredRecipesFor("black", consumer)
  }

  def makeColoredRecipesFor(color: String, consumer: Consumer[FinishedRecipe]): Unit = {
    val dye = RecipeHelper.mcItem(s"${color}_dye")
    makeDyeRecipe(color, dye, consumer)

    val concretePowder = RecipeHelper.mcItem(s"${color}_concrete_powder")
    makeConcretePowderRecipe(dye, concretePowder, consumer)
    makeConcreteMixerRecipe(concretePowder, RecipeHelper.mcItem(s"${color}_concrete"), consumer)
  }

  def makeDyeRecipe(color: String, output: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    ProcessingRecipeBuilder(Recipes.pulverizer.serializer)
      .withInput(Ingredient.of(CustomTags.flowers(color)))
      .withOutput(output, 4)
      .build(s"dyes/$color")
      .save(consumer)
  }

  def makeConcretePowderRecipe(dye: Item, powder: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    ShapelessRecipeBuilder.shapeless(powder, 16)
      .requires(dye)
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
}
