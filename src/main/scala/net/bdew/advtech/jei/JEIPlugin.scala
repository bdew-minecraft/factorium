package net.bdew.advtech.jei

import mezz.jei.api.helpers.{IGuiHelper, IJeiHelpers}
import mezz.jei.api.registration.{IRecipeCatalystRegistration, IRecipeCategoryRegistration, IRecipeRegistration}
import mezz.jei.api.{IModPlugin, JeiPlugin}
import net.bdew.advtech.AdvTech
import net.bdew.lib.Client
import net.bdew.lib.gui.{DrawTarget, SimpleDrawTarget}
import net.minecraft.client.gui.Font
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

@JeiPlugin
class JEIPlugin extends IModPlugin {
  override def getPluginUid: ResourceLocation = new ResourceLocation(AdvTech.ModId, "jei")

  override def registerCategories(registration: IRecipeCategoryRegistration): Unit = {
    JEIPlugin.helpers = registration.getJeiHelpers
    registration.addRecipeCategories(
      CrusherRecipes,
    )
  }

  override def registerRecipes(registration: IRecipeRegistration): Unit = {
    CrusherRecipes.initRecipes(registration)
  }

  override def registerRecipeCatalysts(registration: IRecipeCatalystRegistration): Unit = {
    registration.addRecipeCatalyst(new ItemStack(CrusherRecipes.block), CrusherRecipes.getUid)
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