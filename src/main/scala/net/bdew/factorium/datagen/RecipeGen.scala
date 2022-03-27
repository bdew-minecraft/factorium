package net.bdew.factorium.datagen

import net.bdew.factorium.metals.{MetalItemType, Metals}
import net.minecraft.data.DataGenerator
import net.minecraft.data.recipes._

import java.util.function.Consumer

class RecipeGen(gen: DataGenerator) extends RecipeProvider(gen) {
  override def buildCraftingRecipes(consumer: Consumer[FinishedRecipe]): Unit = {
    for (metal <- Metals.all) {
      RecipeGenCrafting.addCraftingRecipes(metal, consumer)
      RecipeGenProcessing.addProcessingRecipes(metal, consumer)
      RecipeGenExtruder.addExtruderRecipes(metal, consumer)
      if (metal.haveItem(MetalItemType.Ingot) && MetalItemType.smeltables.exists(metal.ownItem))
        RecipeGenSmelting.addSmeltingRecipes(metal, consumer)
    }

    RecipeGenMixer.addConcreteRecipes(consumer)
  }
}
