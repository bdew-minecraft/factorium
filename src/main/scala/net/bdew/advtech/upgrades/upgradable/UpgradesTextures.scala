package net.bdew.advtech.upgrades.upgradable

import net.bdew.advtech.AdvTech
import net.bdew.lib.gui.{Sprite, Texture}
import net.minecraft.resources.ResourceLocation

object UpgradesTextures {
  val image = new ResourceLocation(AdvTech.ModId, "textures/gui/upgrades.png")
  val screen: Sprite = Texture(image, 0, 0, 176, 166)

  val infoIcons = Map(
    InfoEntryKind.CycleLength -> Texture(image, 184, 8, 16, 16),
    InfoEntryKind.EnergyUsed -> Texture(image, 200, 8, 16, 16),
    InfoEntryKind.ItemsPerCycle -> Texture(image, 216, 8, 16, 16),
  )
}
