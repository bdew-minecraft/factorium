package net.bdew.factorium.datagen

import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.tags.FluidTags
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.{Item, Items}

import java.util.function.Consumer

object RecipeGenMixer {
  def addConcreteRecipe(powder: Item, block: Item, consumer: Consumer[FinishedRecipe]): Unit = {
    MixerRecipeBuilder()
      .withInputItem(Ingredient.of(powder))
      .withInputFluid(FluidTags.WATER, 250)
      .withOutputItem(block)
      .build(s"mixer/concrete/${block.getRegistryName.getPath}")
      .save(consumer)
  }

  def addConcreteRecipes(consumer: Consumer[FinishedRecipe]): Unit = {
    addConcreteRecipe(Items.WHITE_CONCRETE_POWDER, Items.WHITE_CONCRETE, consumer)
    addConcreteRecipe(Items.ORANGE_CONCRETE_POWDER, Items.ORANGE_CONCRETE, consumer)
    addConcreteRecipe(Items.MAGENTA_CONCRETE_POWDER, Items.MAGENTA_CONCRETE, consumer)
    addConcreteRecipe(Items.LIGHT_BLUE_CONCRETE_POWDER, Items.LIGHT_BLUE_CONCRETE, consumer)
    addConcreteRecipe(Items.YELLOW_CONCRETE_POWDER, Items.YELLOW_CONCRETE, consumer)
    addConcreteRecipe(Items.LIME_CONCRETE_POWDER, Items.LIME_CONCRETE, consumer)
    addConcreteRecipe(Items.PINK_CONCRETE_POWDER, Items.PINK_CONCRETE, consumer)
    addConcreteRecipe(Items.GRAY_CONCRETE_POWDER, Items.GRAY_CONCRETE, consumer)
    addConcreteRecipe(Items.LIGHT_GRAY_CONCRETE_POWDER, Items.LIGHT_GRAY_CONCRETE, consumer)
    addConcreteRecipe(Items.CYAN_CONCRETE_POWDER, Items.CYAN_CONCRETE, consumer)
    addConcreteRecipe(Items.PURPLE_CONCRETE_POWDER, Items.PURPLE_CONCRETE, consumer)
    addConcreteRecipe(Items.BLUE_CONCRETE_POWDER, Items.BLUE_CONCRETE, consumer)
    addConcreteRecipe(Items.BROWN_CONCRETE_POWDER, Items.BROWN_CONCRETE, consumer)
    addConcreteRecipe(Items.GREEN_CONCRETE_POWDER, Items.GREEN_CONCRETE, consumer)
    addConcreteRecipe(Items.RED_CONCRETE_POWDER, Items.RED_CONCRETE, consumer)
    addConcreteRecipe(Items.BLACK_CONCRETE_POWDER, Items.BLACK_CONCRETE, consumer)
  }
}
