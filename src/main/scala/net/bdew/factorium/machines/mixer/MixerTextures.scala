package net.bdew.factorium.machines.mixer

import net.bdew.factorium.Factorium
import net.bdew.lib.gui.{Sprite, Texture}
import net.bdew.lib.misc.RSMode
import net.minecraft.resources.ResourceLocation

object MixerTextures {
  val image = new ResourceLocation(Factorium.ModId, "textures/gui/mixer.png")

  val screen: Sprite = Texture(image, 0, 0, 176, 166)

  val buttonBase: Sprite = Texture(image, 178, 2, 16, 16)
  val buttonHover: Sprite = Texture(image, 194, 2, 16, 16)

  val powerFill: Sprite = Texture(image, 178, 52, 12, 52)
  val arrow: Sprite = Texture(image, 192, 53, 24, 16)

  val tankOverlay: Sprite = Texture(image, 179, 107, 16, 39)

  val upgrades: Sprite = Texture(image, 227, 3, 14, 14)
  val ioConfig: Sprite = Texture(image, 211, 3, 14, 14)

  val clearButton: Sprite = Texture(image, 217, 53, 5, 5)

  val rsMode: Map[RSMode.Value, Sprite] = Map(
    RSMode.RS_OFF -> Texture(image, 179, 19, 14, 14),
    RSMode.RS_ON -> Texture(image, 195, 19, 14, 14),
    RSMode.ALWAYS -> Texture(image, 211, 19, 14, 14),
    RSMode.NEVER -> Texture(image, 227, 19, 14, 14),
  )
}
