package net.bdew.advtech.jei

import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.registration.IRecipeRegistration
import net.bdew.advtech.AdvTech
import net.bdew.advtech.machines.processing.ProcessingRecipe
import net.bdew.advtech.machines.processing.crusher.CrusherRecipe
import net.bdew.advtech.registries.{Blocks, Recipes}
import net.bdew.lib.recipes.{MachineRecipeType, RecipeReloadListener}
import net.bdew.lib.{DecFormat, Text}
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

import java.util
import scala.jdk.CollectionConverters._

class ProcessingRecipes[T <: ProcessingRecipe](recipeType: MachineRecipeType[T], cls: Class[T], val block: Block) extends IRecipeCategory[T] {
  override def getUid: ResourceLocation = recipeType.registryName
  override def getRecipeClass: Class[T] = cls
  override def getTitle: Component = block.getName

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      new ResourceLocation(AdvTech.ModId, "textures/gui/machine.png"),
      7, 15, 162, 58
    ).build()

  override def getIcon: IDrawable =
    JEIPlugin.guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(block))

  override def setIngredients(recipe: T, ingredients: IIngredients): Unit = {
    ingredients.setInputIngredients(List(recipe.input).asJava)
    ingredients.setOutputs[ItemStack](VanillaTypes.ITEM,
      List(recipe.output, recipe.secondary, recipe.bonus)
        .filter(_.nonEmpty)
        .map(_.stack).asJava
    )
  }

  override def setRecipe(recipeLayout: IRecipeLayout, recipe: T, ingredients: IIngredients): Unit = {
    val itemStacks = recipeLayout.getItemStacks
    itemStacks.init(0, true, 27, 2)
    itemStacks.init(1, false, 98, 2)
    itemStacks.init(2, false, 98, 20)
    itemStacks.init(3, false, 98, 38)
    itemStacks.addTooltipCallback(
      (slotIndex: Int, input: Boolean, ingredient: ItemStack, tooltip: util.List[Component]) => {
        slotIndex match {
          case 1 =>
            tooltip.add(Text.translate(s"advtech.recipes.primary").withStyle(ChatFormatting.YELLOW))
            tooltip.add(Text.translate(s"advtech.recipes.chance", Text.string(DecFormat.dec2(recipe.output.chance * 100)).withStyle(ChatFormatting.YELLOW)))
          case 2 =>
            tooltip.add(Text.translate(s"advtech.recipes.secondary").withStyle(ChatFormatting.YELLOW))
            tooltip.add(Text.translate(s"advtech.recipes.chance", Text.string(DecFormat.dec2(recipe.secondary.chance * 100)).withStyle(ChatFormatting.YELLOW)))
          case 3 =>
            tooltip.add(Text.translate(s"advtech.recipes.bonus").withStyle(ChatFormatting.YELLOW))
            tooltip.add(Text.translate(s"advtech.recipes.chance", Text.string(DecFormat.dec2(recipe.bonus.chance * 100)).withStyle(ChatFormatting.YELLOW)))
          case _ => // pass
        }

      }
    )
    itemStacks.set(0, recipe.input.getItems.toList.asJava)
    if (recipe.output.nonEmpty)
      itemStacks.set(1, recipe.output.stack)
    if (recipe.secondary.nonEmpty)
      itemStacks.set(2, recipe.secondary.stack)
    if (recipe.bonus.nonEmpty)
      itemStacks.set(3, recipe.bonus.stack)

  }

  override def getTooltipStrings(recipe: T, mouseX: Double, mouseY: Double): util.List[Component] = {
    super.getTooltipStrings(recipe, mouseX, mouseY)
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    reg.addRecipes(recipeType.getAllRecipes(RecipeReloadListener.clientRecipeManager).asJava, getUid)
  }
}

object CrusherRecipes extends ProcessingRecipes(Recipes.crusherType, classOf[CrusherRecipe], Blocks.crusher.block.get())