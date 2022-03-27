package net.bdew.factorium.machines.mixer

import net.bdew.factorium.machines.sided.SidedItemIOContainer
import net.bdew.factorium.machines.upgradable.UpgradeableContainer
import net.bdew.factorium.network.{ClearableContainer, RsModeConfigurableContainer}
import net.bdew.factorium.registries.Containers
import net.bdew.lib.container.BaseContainer
import net.bdew.lib.container.switchable.{SwitchableContainer, SwitchableSlot}
import net.bdew.lib.data.base.ContainerDataSlots
import net.bdew.lib.misc.RSMode
import net.minecraft.world.entity.player.Inventory
import net.minecraftforge.fluids.FluidStack

class MixerContainer(val te: MixerEntity, playerInventory: Inventory, id: Int)
  extends BaseContainer(te.externalInventory, Containers.mixer.get(), id)
    with ContainerDataSlots with RsModeConfigurableContainer with SidedItemIOContainer with UpgradeableContainer with ClearableContainer {

  override lazy val dataSource: MixerEntity = te

  def initSlots(): Unit = {
    val showMainSlots: () => Boolean = () => getActiveMode == SwitchableContainer.NormalMode
    this.addSlot(new SwitchableSlot(te.externalInventory, te.Slots.input, 53, 36, showMainSlots))
    this.addSlot(new SwitchableSlot(te.externalInventory, te.Slots.output, 106, 36, showMainSlots))
  }

  initSlots()
  initUpgradeSlots(te.upgrades)
  bindPlayerInventory(playerInventory, 8, 84, 142)

  override def setRsMode(mode: RSMode.Value): Unit = {
    te.rsMode := mode
  }

  override def clearBuffers(slot: Int): Unit = {
    slot match {
      case 0 => te.inputTank.setFluid(FluidStack.EMPTY)
      case 1 => te.outputTank.setFluid(FluidStack.EMPTY)
      case _ => // pass
    }
  }
}
