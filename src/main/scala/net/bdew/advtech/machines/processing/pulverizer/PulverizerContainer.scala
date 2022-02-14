package net.bdew.advtech.machines.processing.pulverizer

import net.bdew.advtech.machines.processing.ProcessingMachineContainer
import net.bdew.advtech.registries.Containers
import net.minecraft.world.entity.player.Inventory

class PulverizerContainer(te: PulverizerEntity, playerInventory: Inventory, id: Int)
  extends ProcessingMachineContainer(te, playerInventory, id, Containers.pulverizer.get())
