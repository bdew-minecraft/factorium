package net.bdew.advtech.upgrades.upgradable

import net.bdew.advtech.machines.BaseMachineEntity
import net.minecraft.world.Container

trait UpgradeableMachine extends BaseMachineEntity with InfoSource {
  def upgrades: Container
}
