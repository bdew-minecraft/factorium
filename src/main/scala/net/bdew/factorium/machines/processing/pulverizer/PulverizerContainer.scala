package net.bdew.factorium.machines.processing.pulverizer

import net.bdew.factorium.machines.processing.ProcessingMachineContainer
import net.bdew.factorium.registries.Containers
import net.minecraft.world.entity.player.Inventory

class PulverizerContainer(te: PulverizerEntity, playerInventory: Inventory, id: Int)
  extends ProcessingMachineContainer(te, playerInventory, id, Containers.pulverizer.get())
