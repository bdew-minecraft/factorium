package net.bdew.advtech

import net.bdew.advtech.datagen.DataGeneration
import net.bdew.advtech.machines.MachineRecipes
import net.bdew.advtech.metals.Metals
import net.bdew.advtech.network.NetworkHandler
import net.bdew.advtech.registries.{Blocks, Containers, Items, Recipes}
import net.bdew.advtech.worldgen.WorldGeneration
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

@Mod(AdvTech.ModId)
object AdvTech {
  final val ModId = "advtech"

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
