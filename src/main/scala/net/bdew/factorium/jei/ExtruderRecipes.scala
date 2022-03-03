package net.bdew.factorium.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableAnimated}
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeRegistration}
import net.bdew.factorium.Config
import net.bdew.factorium.machines.MachineRecipes
import net.bdew.factorium.machines.extruder.{ExtruderRecipe, ExtruderTextures}
import net.bdew.factorium.misc.IngredientMulti
import net.bdew.factorium.registries.{Blocks, Recipes}
import net.bdew.lib.Text
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

import java.util
import scala.jdk.CollectionConverters._

object ExtruderRecipes extends IRecipeCategory[ExtruderRecipe] {
  val block: Block = Blocks.extruder.block.get()

  override def getUid: ResourceLocation = Recipes.extruderType.registryName

  override def getRecipeClass: Class[ExtruderRecipe] = classOf[ExtruderRecipe]

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

  override def setIngredients(recipe: ExtruderRecipe, ingredients: IIngredients): Unit = {
    ingredients.setInputIngredients(List(recipe.input.ingredient, recipe.die).asJava)
    ingredients.setOutput[ItemStack](VanillaTypes.ITEM, recipe.output)
  }

  private def listIngredientMulti(ingredients: IngredientMulti): util.List[ItemStack] =
    util.Arrays.asList(ingredients.ingredient.getItems.map(x => {
      val copy = x.copy()
      copy.setCount(ingredients.count)
      copy
    }): _*)

  override def setRecipe(recipeLayout: IRecipeLayout, recipe: ExtruderRecipe, ingredients: IIngredients): Unit = {
    val itemStacks = recipeLayout.getItemStacks
    itemStacks.init(0, true, 36, 2)
    itemStacks.init(1, true, 36, 38)
    itemStacks.init(2, false, 98, 2)
    itemStacks.set(0, recipe.die.getItems.toList.asJava)
    itemStacks.set(1, listIngredientMulti(recipe.input))
    itemStacks.set(2, recipe.output)
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    reg.addRecipes(MachineRecipes.extruder.asJava, getUid)
  }

  def initCatalyst(reg: IRecipeCatalystRegistration): Unit = {
    reg.addRecipeCatalyst(new ItemStack(block), getUid)
  }

  override def draw(recipe: ExtruderRecipe, stack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    super.draw(recipe, stack, mouseX, mouseY)
    arrow.draw(stack, 64, 20)
  }

  override def getTooltipStrings(recipe: ExtruderRecipe, mouseX: Double, mouseY: Double): util.List[Component] = {
    var res = super.getTooltipStrings(recipe, mouseX, mouseY).asScala
    if (mouseX >= 64 && mouseX <= 88 && mouseY >= 20 && mouseY <= 36)
      res :+= Text.amount(Config.Machines.Extruder.baseCycleTicks() / 20f, "seconds")
    res.asJava
  }
}