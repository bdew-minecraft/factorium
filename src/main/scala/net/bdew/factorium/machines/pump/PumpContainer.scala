package net.bdew.factorium.machines.pump

import net.bdew.factorium.machines.upgradable.UpgradeableContainer
import net.bdew.factorium.network.{ClearableContainer, RsModeConfigurableContainer}
import net.bdew.factorium.registries.Containers
import net.bdew.lib.container.BaseContainer
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.misc.RSMode
import net.minecraft.world.entity.player.Inventory
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction

class PumpContainer(val te: PumpEntity, playerInventory: Inventory, id: Int)
  extends BaseContainer(te.upgrades, Containers.pump.get(), id)
    with ContainerDataSlots with RsModeConfigurableContainer with UpgradeableContainer with ClearableContainer {

  override lazy val dataSource: PumpEntity = te

  initUpgradeSlots(te.upgrades)
  bindPlayerInventory(playerInventory, 8, 84, 142)

  override def setRsMode(mode: RSMode.Value): Unit = {
    te.rsMode := mode
  }

  override def clearBuffers(): Unit = {
    te.tank.drain(Int.MaxValue, FluidAction.EXECUTE)
    te.pumpState.change(PumpState.Lowering(1))
  }
}
