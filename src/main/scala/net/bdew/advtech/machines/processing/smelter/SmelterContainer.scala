package net.bdew.advtech.machines.processing.smelter

import net.bdew.advtech.machines.processing.ProcessingMachineContainer
import net.bdew.advtech.registries.Containers
import net.minecraft.world.entity.player.Inventory

class SmelterContainer(te: SmelterEntity, playerInventory: Inventory, id: Int)
  extends ProcessingMachineContainer(te, playerInventory, id, Containers.smelter.get())
