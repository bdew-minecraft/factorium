package net.bdew.factorium.jei

import com.google.common.cache.{CacheBuilder, CacheLoader, LoadingCache}
import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableAnimated}
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.{IFocusGroup, RecipeIngredientRole, RecipeType}
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeRegistration}
import net.bdew.factorium.machines.MachineRecipes
import net.bdew.factorium.machines.alloy.{AlloyRecipe, AlloyTextures}
import net.bdew.factorium.registries.{Blocks, Recipes}
import net.bdew.factorium.{Config, Factorium}
import net.bdew.lib.{Client, Text}
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

import scala.jdk.CollectionConverters._

object AlloyRecipes extends IRecipeCategory[AlloyRecipe] {
  val block: Block = Blocks.alloySmelter.block.get()

  override val getRecipeType: RecipeType[AlloyRecipe] = RecipeType.create(Factorium.ModId, Recipes.alloy.id.getPath, classOf[AlloyRecipe])

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
    JEIPlugin.guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(block))


  override def setRecipe(builder: IRecipeLayoutBuilder, recipe: AlloyRecipe, focuses: IFocusGroup): Unit = {
    builder.addSlot(RecipeIngredientRole.INPUT, 28, 21)
      .addItemStacks(JeiUtils.listIngredientMulti(recipe.input1))
    builder.addSlot(RecipeIngredientRole.INPUT, 46, 21)
      .addItemStacks(JeiUtils.listIngredientMulti(recipe.input2))
    builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 3)
      .addItemStack(recipe.output)
  }

  def workTime(recipe: AlloyRecipe): Float = Config.Machines.AlloySmelter.baseCycleTicks() / recipe.speedMod

  def initRecipes(reg: IRecipeRegistration): Unit = {
    reg.addRecipes(getRecipeType, MachineRecipes.alloy.toList.asJava)
  }

  def initCatalyst(reg: IRecipeCatalystRegistration): Unit = {
    reg.addRecipeCatalyst(new ItemStack(block), getRecipeType)
  }

  override def draw(recipe: AlloyRecipe, recipeSlotsView: IRecipeSlotsView, stack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    Client.fontRenderer.draw(stack, Text.amount(workTime(recipe) / 20f, "seconds"), 20, 48, 0xFF808080)
    arrowCache.getUnchecked(workTime(recipe).round).draw(stack, 68, 20)
  }
}