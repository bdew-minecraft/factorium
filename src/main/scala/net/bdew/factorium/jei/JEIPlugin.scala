package net.bdew.factorium.jei

import mezz.jei.api.helpers.{IGuiHelper, IJeiHelpers}
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeCategoryRegistration, IRecipeRegistration}
import mezz.jei.api.{IModPlugin, JeiPlugin}
import net.bdew.factorium.Factorium
import net.bdew.lib.Client
import net.bdew.lib.gui.{DrawTarget, SimpleDrawTarget}
import net.minecraft.client.gui.Font
import net.minecraft.resources.ResourceLocation

@JeiPlugin
class JEIPlugin extends IModPlugin {
  override def getPluginUid: ResourceLocation = new ResourceLocation(Factorium.ModId, "jei")

  override def registerCategories(registration: IRecipeCategoryRegistration): Unit = {
    JEIPlugin.helpers = registration.getJeiHelpers
    registration.addRecipeCategories(
      CrusherRecipes,
      GrinderRecipes,
      PulverizerRecipes,
      SmelterRecipes,
      AlloyRecipes,
      ExtruderRecipes,
    )
  }

  override def registerRecipes(registration: IRecipeRegistration): Unit = {
    CrusherRecipes.initRecipes(registration)
    GrinderRecipes.initRecipes(registration)
    PulverizerRecipes.initRecipes(registration)
    SmelterRecipes.initRecipes(registration)
    AlloyRecipes.initRecipes(registration)
    ExtruderRecipes.initRecipes(registration)
  }

  override def registerRecipeCatalysts(registration: IRecipeCatalystRegistration): Unit = {
    CrusherRecipes.initCatalyst(registration)
    GrinderRecipes.initCatalyst(registration)
    PulverizerRecipes.initCatalyst(registration)
    SmelterRecipes.initCatalyst(registration)
    AlloyRecipes.initCatalyst(registration)
    ExtruderRecipes.initCatalyst(registration)
  }
}

object JEIPlugin {
  var helpers: IJeiHelpers = _
  def guiHelper: IGuiHelper = helpers.getGuiHelper
  val drawTarget: DrawTarget = new SimpleDrawTarget {
    override def getZLevel: Float = 0
    override def getFontRenderer: Font = Client.fontRenderer
  }
}