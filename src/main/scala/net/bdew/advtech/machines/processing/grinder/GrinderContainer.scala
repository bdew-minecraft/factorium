package net.bdew.advtech.machines.processing.grinder

import net.bdew.advtech.machines.processing.ProcessingMachineContainer
import net.bdew.advtech.registries.Containers
import net.minecraft.world.entity.player.Inventory

class GrinderContainer(te: GrinderEntity, playerInventory: Inventory, id: Int)
  extends ProcessingMachineContainer(te, playerInventory, id, Containers.grinder.get())
