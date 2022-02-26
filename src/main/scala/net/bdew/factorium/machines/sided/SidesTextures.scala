package net.bdew.factorium.machines.sided

import net.bdew.factorium.Factorium
import net.bdew.factorium.misc.AutoIOMode
import net.bdew.lib.gui.{Sprite, Texture}
import net.minecraft.resources.ResourceLocation

object SidesTextures {
  val image = new ResourceLocation(Factorium.ModId, "textures/gui/sides.png")
  val screen: Sprite = Texture(image, 0, 0, 176, 166)

  val ioMode: Map[AutoIOMode.Value, Sprite] = Map(
    AutoIOMode.NONE -> Texture(image, 179, 19, 14, 14),
    AutoIOMode.INPUT -> Texture(image, 195, 19, 14, 14),
    AutoIOMode.OUTPUT -> Texture(image, 211, 19, 14, 14),
    AutoIOMode.BOTH -> Texture(image, 227, 19, 14, 14),
  )
}
