package net.bdew.advtech.machines.processing.crusher

import net.bdew.advtech.machines.processing.ProcessingMachineContainer
import net.bdew.advtech.registries.Containers
import net.minecraft.world.entity.player.Inventory

class CrusherContainer(te: CrusherEntity, playerInventory: Inventory, id: Int)
  extends ProcessingMachineContainer(te, playerInventory, id, Containers.crusher.get())
