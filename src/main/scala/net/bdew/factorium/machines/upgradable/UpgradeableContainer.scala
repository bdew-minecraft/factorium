package net.bdew.factorium.machines.upgradable

import net.bdew.lib.container.switchable.{ContainerMode, SwitchableContainer, SwitchableSlot}

trait UpgradeableContainer extends SwitchableContainer {
  def te: UpgradeableMachine

  addMode(UpgradeableContainer.UpgradesMode)

  def initUpgradeSlots(inv: DataSlotUpgrades): Unit = {
    for (y <- 0 to 1; x <- 0 to 2) {
      this.addSlotInternal(new SwitchableSlot(
        inv,
        x + y * 3,
        15 + x * 18, 25 + y * 18,
        () => getActiveMode == UpgradeableContainer.UpgradesMode
      ))
    }
  }
}

object UpgradeableContainer {
  val UpgradesMode: ContainerMode = ContainerMode("upgrades")
}