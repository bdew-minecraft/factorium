package net.bdew.factorium.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableAnimated}
import mezz.jei.api.gui.ingredient.{IRecipeSlotView, IRecipeSlotsView}
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.{IFocusGroup, RecipeIngredientRole, RecipeType}
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeRegistration}
import net.bdew.factorium.machines.MachineRecipes
import net.bdew.factorium.machines.processing.crusher.CrusherRecipe
import net.bdew.factorium.machines.processing.grinder.GrinderRecipe
import net.bdew.factorium.machines.processing.pulverizer.PulverizerRecipe
import net.bdew.factorium.machines.processing.smelter.SmelterRecipe
import net.bdew.factorium.machines.processing.{ProcessingRecipe, ProcessingTextures}
import net.bdew.factorium.machines.worker.WorkerMachineConfig
import net.bdew.factorium.registries.{Blocks, Recipes}
import net.bdew.factorium.{Config, Factorium}
import net.bdew.lib.recipes.MachineRecipeType
import net.bdew.lib.{DecFormat, Text}
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

import java.util
import java.util.Collections
import scala.jdk.CollectionConverters._

abstract class ProcessingRecipes[T <: ProcessingRecipe](recipeType: MachineRecipeType[T], cls: Class[T]) extends IRecipeCategory[T] {
  def getRecipes: List[T]
  def block: Block
  def cfg: WorkerMachineConfig

  override val getRecipeType: RecipeType[T] = RecipeType.create(Factorium.ModId, recipeType.registryName.getPath, cls)

  override def getTitle: Component = block.getName

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      ProcessingTextures.image,
      7, 15, 162, 58
    ).build()

  lazy private val arrow = JEIPlugin.guiHelper.drawableBuilder(
    ProcessingTextures.image, 192, 53, 24, 16)
    .buildAnimated(cfg.baseCycleTicks().round, IDrawableAnimated.StartDirection.LEFT, false)

  override def getIcon: IDrawable =
    JEIPlugin.guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(block))

  override def setRecipe(builder: IRecipeLayoutBuilder, recipe: T, focuses: IFocusGroup): Unit = {
    builder.addSlot(RecipeIngredientRole.INPUT, 28, 3).addIngredients(recipe.input)

    if (recipe.output.nonEmpty)
      builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 3)
        .addItemStack(recipe.output.stack)
        .addTooltipCallback((recipeSlotView: IRecipeSlotView, tooltip: util.List[Component]) => {
          tooltip.add(Text.translate(s"factorium.recipes.primary").withStyle(ChatFormatting.YELLOW))
          tooltip.add(Text.translate(s"factorium.recipes.chance", Text.string(DecFormat.dec2(recipe.output.chance * 100)).withStyle(ChatFormatting.YELLOW)))
        })

    if (recipe.secondary.nonEmpty)
      builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 21)
        .addItemStack(recipe.secondary.stack)
        .addTooltipCallback((recipeSlotView: IRecipeSlotView, tooltip: util.List[Component]) => {
          tooltip.add(Text.translate(s"factorium.recipes.secondary").withStyle(ChatFormatting.YELLOW))
          tooltip.add(Text.translate(s"factorium.recipes.chance", Text.string(DecFormat.dec2(recipe.secondary.chance * 100)).withStyle(ChatFormatting.YELLOW)))
        })

    if (recipe.bonus.nonEmpty)
      builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 39)
        .addItemStack(recipe.bonus.stack)
        .addTooltipCallback((recipeSlotView: IRecipeSlotView, tooltip: util.List[Component]) => {
          tooltip.add(Text.translate(s"factorium.recipes.bonus").withStyle(ChatFormatting.YELLOW))
          tooltip.add(Text.translate(s"factorium.recipes.chance", Text.string(DecFormat.dec2(recipe.bonus.chance * 100)).withStyle(ChatFormatting.YELLOW)))
        })
  }

  override def getTooltipStrings(recipe: T, recipeSlotsView: IRecipeSlotsView, mouseX: Double, mouseY: Double): util.List[Component] = {
    if (mouseX >= 68 && mouseX <= 92 && mouseY >= 20 && mouseY <= 36)
      Collections.singletonList(Text.amount(cfg.baseCycleTicks() / 20f, "seconds"))
    else
      Collections.emptyList
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    reg.addRecipes(getRecipeType, getRecipes.asJava)
  }

  def initCatalyst(reg: IRecipeCatalystRegistration): Unit = {
    reg.addRecipeCatalyst(new ItemStack(block), getRecipeType)
  }

  override def draw(recipe: T, recipeSlotsView: IRecipeSlotsView, stack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    arrow.draw(stack, 68, 20)
  }
}

object CrusherRecipes extends ProcessingRecipes(Recipes.crusher.recipeType, classOf[CrusherRecipe]) {
  override def block: Block = Blocks.crusher.block.get()
  override def getRecipes: List[CrusherRecipe] = MachineRecipes.crusher.toList
  override def cfg: WorkerMachineConfig = Config.Machines.Crusher
}

object GrinderRecipes extends ProcessingRecipes(Recipes.grinder.recipeType, classOf[GrinderRecipe]) {
  override def block: Block = Blocks.grinder.block.get()
  override def getRecipes: List[GrinderRecipe] = MachineRecipes.grinder.toList
  override def cfg: WorkerMachineConfig = Config.Machines.Grinder
}

object PulverizerRecipes extends ProcessingRecipes(Recipes.pulverizer.recipeType, classOf[PulverizerRecipe]) {
  override def block: Block = Blocks.pulverizer.block.get()
  override def getRecipes: List[PulverizerRecipe] = MachineRecipes.pulverizer.toList
  override def cfg: WorkerMachineConfig = Config.Machines.Pulverizer
}

object SmelterRecipes extends ProcessingRecipes(Recipes.smelter.recipeType, classOf[SmelterRecipe]) {
  override def block: Block = Blocks.smelter.block.get()
  override def getRecipes: List[SmelterRecipe] = MachineRecipes.smelter.toList
  override def cfg: WorkerMachineConfig = Config.Machines.Smelter
}