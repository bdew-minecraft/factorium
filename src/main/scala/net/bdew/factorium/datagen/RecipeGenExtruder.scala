package net.bdew.factorium.datagen

import net.bdew.factorium.metals.{MetalEntry, MetalItemType}
import net.bdew.factorium.registries.Items
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.world.item.crafting.Ingredient

import java.util.function.Consumer

object RecipeGenExtruder {
  def addExtruderRecipes(metal: MetalEntry, consumer: Consumer[FinishedRecipe]): Unit = {
    makeExtruderRecipe(metal, output = MetalItemType.Rod, inputCount = 1, outputCount = 2, consumer)
    makeExtruderRecipe(metal, output = MetalItemType.Plate, inputCount = 1, outputCount = 1, consumer)
    makeExtruderRecipe(metal, output = MetalItemType.Wire, inputCount = 1, outputCount = 2, consumer)
    makeExtruderRecipe(metal, output = MetalItemType.Gear, inputCount = 4, outputCount = 1, consumer)
    makeExtruderRecipe(metal, output = MetalItemType.Nugget, inputCount = 1, outputCount = 9, consumer)
  }

  def makeExtruderRecipe(metal: MetalEntry, output: MetalItemType, inputCount: Int, outputCount: Int, consumer: Consumer[FinishedRecipe]): Unit = {
    if (!metal.haveItem(output)) return
    val ingotTag = CustomTags.ingots(metal.name)
    ExtruderRecipeBuilder()
      .withInput(Ingredient.of(ingotTag), inputCount)
      .withDie(Ingredient.of(Items.dies(output.kind).get()))
      .withOutput(metal.item(output), outputCount)
      .requireTag(ingotTag)
      .build(s"metals/${metal.name}/${output.kind}_from_extruder")
      .save(consumer)
  }
}
