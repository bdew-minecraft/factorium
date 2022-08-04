package net.bdew.factorium

import net.bdew.factorium.datagen.DataGeneration
import net.bdew.factorium.machines.MachineRecipes
import net.bdew.factorium.metals.Metals
import net.bdew.factorium.network.NetworkHandler
import net.bdew.factorium.registries.{Blocks, Containers, Items, Recipes}
import net.bdew.factorium.worldgen.WorldGeneration
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

@Mod(Factorium.ModId)
class Factorium {
  Config.init()
  Items.init()
  Blocks.init()
  Metals.init()
  Recipes.init()
  Containers.init()
  MachineRecipes.init()
  NetworkHandler.init()
  WorldGeneration.init()

  FMLJavaModLoadingContext.get().getModEventBus.addListener(DataGeneration.onGatherData)
}

object Factorium {
  final val ModId = "factorium"
}