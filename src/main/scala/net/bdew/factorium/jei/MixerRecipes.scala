package net.bdew.factorium.jei

import com.mojang.blaze3d.vertex.PoseStack
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.{IDrawable, IDrawableAnimated, IDrawableStatic}
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.{IFocusGroup, RecipeIngredientRole, RecipeType}
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeRegistration}
import net.bdew.factorium.machines.MachineRecipes
import net.bdew.factorium.machines.mixer.{MixerConfig, MixerRecipe, MixerTextures}
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

object MixerRecipes extends IRecipeCategory[MixerRecipe] {
  val block: Block = Blocks.mixer.block.get()
  val config: MixerConfig = Config.Machines.Mixer

  override val getRecipeType: RecipeType[MixerRecipe] = RecipeType.create(Factorium.ModId, Recipes.mixer.id.getPath, classOf[MixerRecipe])

  override def getUid: ResourceLocation = getRecipeType.getUid
  override def getRecipeClass: Class[_ <: MixerRecipe] = getRecipeType.getRecipeClass

  override def getTitle: Component = block.getName

  lazy private val arrow = JEIPlugin.guiHelper.drawableBuilder(
    MixerTextures.image, 192, 53, 24, 16)
    .buildAnimated(config.baseCycleTicks().round, IDrawableAnimated.StartDirection.LEFT, false)

  private val fluidOverlay: IDrawableStatic = JEIPlugin.guiHelper.createDrawable(
    MixerTextures.image, 179, 107, 16, 39
  )

  override def getBackground: IDrawable =
    JEIPlugin.guiHelper.drawableBuilder(
      MixerTextures.image,
      7, 15, 162, 58
    ).build()

  override def getIcon: IDrawable =
    JEIPlugin.guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(block))

  override def setRecipe(builder: IRecipeLayoutBuilder, recipe: MixerRecipe, focuses: IFocusGroup): Unit = {
    builder.addSlot(RecipeIngredientRole.INPUT, 46, 21)
      .addIngredients(VanillaTypes.ITEM, JeiUtils.listIngredientMulti(recipe.inputItem))

    builder.addSlot(RecipeIngredientRole.INPUT, 24, 9)
      .addIngredients(VanillaTypes.FLUID, JeiUtils.listFluidStackIngredient(recipe.inputFluid))
      .setFluidRenderer(config.tankCapacity(), false, 16, 39)
      .setOverlay(fluidOverlay, 0, 0)

    if (!recipe.outputItem.isEmpty)
      builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 21)
        .addIngredient(VanillaTypes.ITEM, recipe.outputItem)

    if (!recipe.outputFluid.isEmpty)
      builder.addSlot(RecipeIngredientRole.OUTPUT, 121, 9)
        .addIngredient(VanillaTypes.FLUID, recipe.outputFluid.toStack)
        .setFluidRenderer(config.tankCapacity(), false, 16, 39)
        .setOverlay(fluidOverlay, 0, 0)

  }

  def initRecipes(reg: IRecipeRegistration): Unit = {
    reg.addRecipes(getRecipeType, MachineRecipes.mixer.toList.asJava)
  }

  def initCatalyst(reg: IRecipeCatalystRegistration): Unit = {
    reg.addRecipeCatalyst(new ItemStack(block), getRecipeType)
  }

  override def draw(recipe: MixerRecipe, recipeSlotsView: IRecipeSlotsView, stack: PoseStack, mouseX: Double, mouseY: Double): Unit = {
    arrow.draw(stack, 68, 20)
  }

  override def getTooltipStrings(recipe: MixerRecipe, recipeSlotsView: IRecipeSlotsView, mouseX: Double, mouseY: Double): util.List[Component] = {
    if (mouseX >= 64 && mouseX <= 88 && mouseY >= 20 && mouseY <= 36)
      Collections.singletonList(Text.amount(Config.Machines.Mixer.baseCycleTicks() / 20f, "seconds"))
    else
      Collections.emptyList
  }
}