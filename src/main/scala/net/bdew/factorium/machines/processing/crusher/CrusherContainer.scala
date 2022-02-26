package net.bdew.factorium.machines.processing.crusher

import net.bdew.factorium.machines.processing.ProcessingMachineContainer
import net.bdew.factorium.registries.Containers
import net.minecraft.world.entity.player.Inventory

class CrusherContainer(te: CrusherEntity, playerInventory: Inventory, id: Int)
  extends ProcessingMachineContainer(te, playerInventory, id, Containers.crusher.get())
