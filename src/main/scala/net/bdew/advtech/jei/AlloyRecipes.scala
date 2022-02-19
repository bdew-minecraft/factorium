package net.bdew.advtech.jei

import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeRegistration}
import net.bdew.advtech.machines.MachineRecipes
import net.bdew.advtech.machines.alloy.{AlloyRecipe, AlloyTextures}
import net.bdew.advtech.misc.IngredientMulti
import net.bdew.advtech.registries.{Blocks, Recipes}
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

import java.util
import scala.jdk.CollectionConverters._

object AlloyRecipes extends IRecipeCategory[AlloyRecipe] {
  val block: Block = Blocks.alloySmelter.block.get()

  override def getUid: ResourceLocation = Recipes.alloyType.registryName
  override def getRecipeClass: Class[AlloyRecipe] = classOf[AlloyRecipe]
  override def getTitle: Component = block.getName

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      AlloyTextures.image,
      7, 15, 162, 58
    ).build()

  override def getIcon: IDrawable =
    JEIPlugin.guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(block))

  override def setIngredients(recipe: AlloyRecipe, ingredients: IIngredients): Unit = {
    ingredients.setInputIngredients(List(recipe.input1.ingredient, recipe.input2.ingredient).asJava)
    ingredients.setOutput[ItemStack](VanillaTypes.ITEM, recipe.output)
  }

  def listIngredientMulti(ingr: IngredientMulti) =
    util.Arrays.asList(ingr.ingredient.getItems.map(x => {
      val copy = x.copy()
      copy.setCount(ingr.count)
      copy
    }): _*)

  override def setRecipe(recipeLayout: IRecipeLayout, recipe: AlloyRecipe, ingredients: IIngredients): Unit = {
    val itemStacks = recipeLayout.getItemStacks
    itemStacks.init(0, true, 27, 20)
    itemStacks.init(1, true, 45, 20)
    itemStacks.init(2, false, 98, 2)
    itemStacks.set(0, listIngredientMulti(recipe.input1))
    itemStacks.set(1, listIngredientMulti(recipe.input2))
    itemStacks.set(2, recipe.output)
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    reg.addRecipes(MachineRecipes.alloy.asJava, getUid)
  }

  def initCatalyst(reg: IRecipeCatalystRegistration): Unit = {
    reg.addRecipeCatalyst(new ItemStack(block), getUid)
  }
}