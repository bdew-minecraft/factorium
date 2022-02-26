package net.bdew.factorium.machines.upgradable

import net.bdew.factorium.machines.BaseMachineEntity
import net.bdew.factorium.upgrades.UpgradeClass

trait UpgradeableMachine extends BaseMachineEntity with InfoSource {
  def upgrades: DataSlotUpgrades
  def validUpgradeClasses: Set[UpgradeClass]
}
