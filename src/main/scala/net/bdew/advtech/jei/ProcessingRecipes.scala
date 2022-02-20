package net.bdew.advtech.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableAnimated}
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeRegistration}
import net.bdew.advtech.Config
import net.bdew.advtech.machines.MachineRecipes
import net.bdew.advtech.machines.processing.crusher.CrusherRecipe
import net.bdew.advtech.machines.processing.grinder.GrinderRecipe
import net.bdew.advtech.machines.processing.pulverizer.PulverizerRecipe
import net.bdew.advtech.machines.processing.smelter.SmelterRecipe
import net.bdew.advtech.machines.processing.{ProcessingRecipe, ProcessingTextures}
import net.bdew.advtech.machines.worker.WorkerMachineConfig
import net.bdew.advtech.registries.{Blocks, Recipes}
import net.bdew.lib.recipes.MachineRecipeType
import net.bdew.lib.{DecFormat, Text}
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

import java.util
import scala.jdk.CollectionConverters._

abstract class ProcessingRecipes[T <: ProcessingRecipe](recipeType: MachineRecipeType[T], cls: Class[T]) extends IRecipeCategory[T] {
  def getRecipes: List[T]
  def block: Block
  def cfg: WorkerMachineConfig

  override def getUid: ResourceLocation = recipeType.registryName
  override def getRecipeClass: Class[T] = cls
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
    var res = super.getTooltipStrings(recipe, mouseX, mouseY).asScala
    if (mouseX >= 68 && mouseX <= 92 && mouseY >= 20 && mouseY <= 36)
      res :+= Text.amount(cfg.baseCycleTicks() / 20f, "seconds")
    res.asJava
  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    reg.addRecipes(getRecipes.asJava, getUid)
  }

  def initCatalyst(reg: IRecipeCatalystRegistration): Unit = {
    reg.addRecipeCatalyst(new ItemStack(block), getUid)
  }

  override def draw(recipe: T, stack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    super.draw(recipe, stack, mouseX, mouseY)
    arrow.draw(stack, 68, 20)
  }
}

object CrusherRecipes extends ProcessingRecipes(Recipes.crusherType, classOf[CrusherRecipe]) {
  override def block: Block = Blocks.crusher.block.get()
  override def getRecipes: List[CrusherRecipe] = MachineRecipes.crusher.toList
  override def cfg: WorkerMachineConfig = Config.Machines.Crusher
}

object GrinderRecipes extends ProcessingRecipes(Recipes.grinderType, classOf[GrinderRecipe]) {
  override def block: Block = Blocks.grinder.block.get()
  override def getRecipes: List[GrinderRecipe] = MachineRecipes.grinder.toList
  override def cfg: WorkerMachineConfig = Config.Machines.Grinder
}

object PulverizerRecipes extends ProcessingRecipes(Recipes.pulverizerType, classOf[PulverizerRecipe]) {
  override def block: Block = Blocks.pulverizer.block.get()
  override def getRecipes: List[PulverizerRecipe] = MachineRecipes.pulverizer.toList
  override def cfg: WorkerMachineConfig = Config.Machines.Pulverizer
}

object SmelterRecipes extends ProcessingRecipes(Recipes.smelterType, classOf[SmelterRecipe]) {
  override def block: Block = Blocks.smelter.block.get()
  override def getRecipes: List[SmelterRecipe] = MachineRecipes.smelter.toList
  override def cfg: WorkerMachineConfig = Config.Machines.Smelter
}