package net.bdew.factorium.jei

import net.bdew.factorium.misc.IngredientMulti
import net.bdew.lib.recipes.FluidStackIngredient
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

import java.util
import scala.jdk.CollectionConverters._

object JeiUtils {
  def listIngredientMulti(ingredients: IngredientMulti): util.List[ItemStack] =
    util.Arrays.asList(ingredients.ingredient.getItems.map(x => {
      val copy = x.copy()
      copy.setCount(ingredients.count)
      copy
    }): _*)

  def listFluidStackIngredient(ingredient: FluidStackIngredient): util.List[FluidStack] = {
    ingredient.fluids.map(x => new FluidStack(x, ingredient.amount)).toList.asJava
  }
}
