package net.bdew.advtech.machines

import net.bdew.advtech.AdvTech
import net.bdew.lib.gui._
import net.bdew.lib.misc.RSMode
import net.minecraft.resources.ResourceLocation

object MachineTextures {
  val image = new ResourceLocation(AdvTech.ModId, "textures/gui/machine.png")

  val screen: Sprite = Texture(image, 0, 0, 176, 166)

  val buttonBase: Sprite = Texture(image, 178, 2, 16, 16)
  val buttonHover: Sprite = Texture(image, 194, 2, 16, 16)

  val powerFill: Sprite = Texture(image, 178, 52, 12, 52)
  val arrow: Sprite = Texture(image, 192, 53, 24, 16)

  val upgrades: Sprite = Texture(image, 227, 3, 14, 14)
  val ioConfig: Sprite = Texture(image, 211, 3, 14, 14)

  val rsMode: Map[RSMode.Value, Sprite] = Map(
    RSMode.RS_OFF -> Texture(image, 179, 19, 14, 14),
    RSMode.RS_ON -> Texture(image, 195, 19, 14, 14),
    RSMode.ALWAYS -> Texture(image, 211, 19, 14, 14),
    RSMode.NEVER -> Texture(image, 227, 19, 14, 14),
  )
}
