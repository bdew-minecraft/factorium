package net.bdew.advtech.jei

import com.google.common.cache.{CacheBuilder, CacheLoader, LoadingCache}
import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableAnimated}
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeRegistration}
import net.bdew.advtech.Config
import net.bdew.advtech.machines.MachineRecipes
import net.bdew.advtech.machines.alloy.{AlloyRecipe, AlloyTextures}
import net.bdew.advtech.misc.IngredientMulti
import net.bdew.advtech.registries.{Blocks, Recipes}
import net.bdew.lib.{Client, Text}
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

  lazy private val arrowCache: LoadingCache[Integer, IDrawableAnimated] =
    CacheBuilder.newBuilder.maximumSize(25).build(new CacheLoader[Integer, IDrawableAnimated] {
      override def load(time: Integer): IDrawableAnimated =
        JEIPlugin.guiHelper.drawableBuilder(
          AlloyTextures.image, 192, 53, 24, 16)
          .buildAnimated(time, IDrawableAnimated.StartDirection.LEFT, false)
    })

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

  private def listIngredientMulti(ingredients: IngredientMulti): util.List[ItemStack] =
    util.Arrays.asList(ingredients.ingredient.getItems.map(x => {
      val copy = x.copy()
      copy.setCount(ingredients.count)
      copy
    }): _*)

  def workTime(recipe: AlloyRecipe): Float = Config.Machines.AlloySmelter.baseCycleTicks() / recipe.speedMod

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

  override def draw(recipe: AlloyRecipe, stack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    super.draw(recipe, stack, mouseX, mouseY)
    Client.fontRenderer.draw(stack, Text.amount(workTime(recipe) / 20f, "seconds"), 20, 48, 0xFF808080)
    arrowCache.getUnchecked(workTime(recipe).round).draw(stack, 68, 20)
  }
}