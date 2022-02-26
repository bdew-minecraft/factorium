package net.bdew.factorium.machines.upgradable

import net.bdew.factorium.machines.BaseMachineEntity

trait UpgradeableMachine extends BaseMachineEntity with InfoSource {
  def upgrades: DataSlotUpgrades
}
