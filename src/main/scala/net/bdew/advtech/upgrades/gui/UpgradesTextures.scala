package net.bdew.advtech.upgrades.gui

import net.bdew.advtech.AdvTech
import net.bdew.lib.gui.{Sprite, Texture}
import net.minecraft.resources.ResourceLocation

object UpgradesTextures {
  val image = new ResourceLocation(AdvTech.ModId, "textures/gui/upgrades.png")
  val screen: Sprite = Texture(image, 0, 0, 176, 166)
}
