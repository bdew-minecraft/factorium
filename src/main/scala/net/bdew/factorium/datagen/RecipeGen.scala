package net.bdew.factorium.datagen

import net.bdew.factorium.metals.{MetalItemType, Metals}
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes._

import java.util.function.Consumer

class RecipeGen(out: PackOutput) extends RecipeProvider(out) {
  override def buildRecipes(consumer: Consumer[FinishedRecipe]): Unit = {
    for (metal <- Metals.all) {
      RecipeGenCrafting.addCraftingRecipes(metal, consumer)
      RecipeGenProcessing.addProcessingRecipes(metal, consumer)
      RecipeGenExtruder.addExtruderRecipes(metal, consumer)
      if (metal.haveItem(MetalItemType.Ingot) && MetalItemType.smeltables.exists(metal.ownItem))
        RecipeGenSmelting.addSmeltingRecipes(metal, consumer)
    }

    RecipeGenColored.makeColoredRecipes(consumer)
  }
}
