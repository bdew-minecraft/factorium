package net.bdew.factorium.client

import net.bdew.factorium.Factorium
import net.bdew.lib.render.IconPreloader
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus

@Mod.EventBusSubscriber(modid = Factorium.ModId, value = Array(Dist.CLIENT), bus = Bus.MOD)
object MiscTextures extends IconPreloader {
  val pumpHose: TextureLoc = TextureLoc(s"${Factorium.ModId}:entities/pump_hose")
}
