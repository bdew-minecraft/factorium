package net.bdew.factorium.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableAnimated}
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.{IFocusGroup, RecipeIngredientRole, RecipeType}
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeRegistration}
import net.bdew.factorium.machines.MachineRecipes
import net.bdew.factorium.machines.extruder.{ExtruderRecipe, ExtruderTextures}
import net.bdew.factorium.registries.{Blocks, Recipes}
import net.bdew.factorium.{Config, Factorium}
import net.bdew.lib.Text
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

import java.util
import java.util.Collections
import scala.jdk.CollectionConverters._

object ExtruderRecipes extends IRecipeCategory[ExtruderRecipe] {
  val block: Block = Blocks.extruder.block.get()

  override val getRecipeType: RecipeType[ExtruderRecipe] = RecipeType.create(Factorium.ModId, Recipes.extruder.id.getPath, classOf[ExtruderRecipe])

  override def getUid: ResourceLocation = getRecipeType.getUid
  override def getRecipeClass: Class[_ <: ExtruderRecipe] = getRecipeType.getRecipeClass

  override def getTitle: Component = block.getName

  lazy private val arrow = JEIPlugin.guiHelper.drawableBuilder(
    ExtruderTextures.image, 192, 53, 24, 16)
    .buildAnimated(Config.Machines.Extruder.baseCycleTicks().round, IDrawableAnimated.StartDirection.LEFT, false)

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      ExtruderTextures.image,
      7, 15, 162, 58
    ).build()

  override def getIcon: IDrawable =
    JEIPlugin.guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(block))

  override def setRecipe(builder: IRecipeLayoutBuilder, recipe: ExtruderRecipe, focuses: IFocusGroup): Unit = {
    builder.addSlot(RecipeIngredientRole.INPUT, 37, 3)
      .addIngredients(recipe.die)
    builder.addSlot(RecipeIngredientRole.INPUT, 37, 39)
      .addItemStacks(JeiUtils.listIngredientMulti(recipe.input))
    builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 3)
      .addItemStack(recipe.output)
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    reg.addRecipes(getRecipeType, MachineRecipes.extruder.toList.asJava)
  }

  def initCatalyst(reg: IRecipeCatalystRegistration): Unit = {
    reg.addRecipeCatalyst(new ItemStack(block), getRecipeType)
  }

  override def draw(recipe: ExtruderRecipe, recipeSlotsView: IRecipeSlotsView, stack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    arrow.draw(stack, 64, 20)
  }

  override def getTooltipStrings(recipe: ExtruderRecipe, recipeSlotsView: IRecipeSlotsView, mouseX: Double, mouseY: Double): util.List[Component] = {
    if (mouseX >= 64 && mouseX <= 88 && mouseY >= 20 && mouseY <= 36)
      Collections.singletonList(Text.amount(Config.Machines.Extruder.baseCycleTicks() / 20f, "seconds"))
    else
      Collections.emptyList
  }
}