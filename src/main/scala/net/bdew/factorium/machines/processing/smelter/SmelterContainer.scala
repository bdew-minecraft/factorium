package net.bdew.factorium.machines.processing.smelter

import net.bdew.factorium.machines.processing.ProcessingMachineContainer
import net.bdew.factorium.registries.Containers
import net.minecraft.world.entity.player.Inventory

class SmelterContainer(te: SmelterEntity, playerInventory: Inventory, id: Int)
  extends ProcessingMachineContainer(te, playerInventory, id, Containers.smelter.get())
