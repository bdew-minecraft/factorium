package net.bdew.factorium.client

import net.bdew.lib.render.connected.ConnectedModelEnhancer
import net.bdew.lib.render.models.EnhancedModelLoader
import net.minecraft.resources.ResourceLocation

object CustomModels {
  val connectedModelLoader = new EnhancedModelLoader(new ConnectedModelEnhancer(new ResourceLocation("_ex", "frame")))
}
