package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.registries.{Blocks, Items, Recipes}
import net.minecraft.data.recipes.{FinishedRecipe, RecipeBuilder, RecipeCategory, ShapelessRecipeBuilder}
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.FluidTags
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.{DyeColor, Item, Items => MCItems}
import net.minecraftforge.common.Tags
import net.minecraftforge.registries.ForgeRegistries

import java.util.function.Consumer

object RecipeGenColored {
  def makeColoredRecipes(consumer: Consumer[FinishedRecipe]): Unit = {
    DyeColor.values().foreach(x => makeColoredRecipesFor(x, consumer))
  }

  def makeColoredRecipesFor(color: DyeColor, consumer: Consumer[FinishedRecipe]): Unit = {

    makeDyeRecipe(color.getName, consumer)

    val concretePowder = RecipeHelper.mcItem(s"${color}_concrete_powder")
    makeConcretePowderRecipe(color.getName, concretePowder, consumer)
    makeConcreteMixerRecipe(concretePowder, RecipeHelper.mcItem(s"${color}_concrete"), consumer)

    makeConcreteGlowPowderRecipe(color, concretePowder, consumer)
    makeConcreteReinforcedPowderRecipe(color, concretePowder, consumer)

    makeConcreteMixerRecipe(Blocks.reinforcedConcretePowder(color).get().asItem(), Blocks.reinforcedConcrete(color).get().asItem(), consumer)
    makeConcreteMixerRecipe(Blocks.glowingConcretePowder(color).get().asItem(), Blocks.glowingConcrete(color).get().asItem(), consumer)

    makeWoolToStringRecipe(color.getName, consumer)
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
    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, powder, 16)
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
      .save(consumer, new ResourceLocation(Factorium.ModId, s"concrete/powder/normal/${color}"))
  }


  def makeConcreteGlowPowderRecipe(color: DyeColor, powder: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.glowingConcretePowder(color).get(), 4)
      .requires(Ingredient.of(powder), 4)
      .requires(Ingredient.of(CustomTags.dusts("glowstone")))
      .asInstanceOf[RecipeBuilder]
      .unlockedBy("has_glowstone", RecipeHelper.has(CustomTags.dusts("glowstone")))
      .unlockedBy("has_powder", RecipeHelper.has(powder))
      .group("concrete_powder")
      .save(consumer, new ResourceLocation(Factorium.ModId, s"concrete/powder/glowing/${color.getName}"))
  }

  def makeConcreteReinforcedPowderRecipe(color: DyeColor, powder: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    val mesh = Items.craftItems("mesh_reinforced").get
    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.reinforcedConcretePowder(color).get())
      .requires(Ingredient.of(powder))
      .requires(Ingredient.of(mesh))
      .asInstanceOf[RecipeBuilder]
      .unlockedBy("has_mesh", RecipeHelper.has(mesh))
      .unlockedBy("has_powder", RecipeHelper.has(powder))
      .group("concrete_powder")
      .save(consumer, new ResourceLocation(Factorium.ModId, s"concrete/powder/reinforced/${color.getName}"))
  }

  def makeConcreteMixerRecipe(powder: Item, item: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    MixerRecipeBuilder()
      .withInputItem(Ingredient.of(powder))
      .withInputFluid(FluidTags.WATER, 250)
      .withOutputItem(item)
      .build(s"concrete/mixer/${ForgeRegistries.ITEMS.getKey(item).getPath}")
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
