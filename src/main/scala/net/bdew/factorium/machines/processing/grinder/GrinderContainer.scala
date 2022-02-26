package net.bdew.factorium.machines.processing.grinder

import net.bdew.factorium.machines.processing.ProcessingMachineContainer
import net.bdew.factorium.registries.Containers
import net.minecraft.world.entity.player.Inventory

class GrinderContainer(te: GrinderEntity, playerInventory: Inventory, id: Int)
  extends ProcessingMachineContainer(te, playerInventory, id, Containers.grinder.get())
