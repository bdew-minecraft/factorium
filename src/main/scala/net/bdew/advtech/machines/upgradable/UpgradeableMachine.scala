package net.bdew.advtech.machines.upgradable

import net.bdew.advtech.machines.BaseMachineEntity

trait UpgradeableMachine extends BaseMachineEntity with InfoSource {
  def upgrades: DataSlotUpgrades
}
