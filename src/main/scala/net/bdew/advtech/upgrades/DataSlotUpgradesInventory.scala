package net.bdew.advtech.upgrades

import net.bdew.lib.data.DataSlotInventory

class DataSlotUpgradesInventory(parent: UpgradeableMachine) extends DataSlotInventory("upgrades", parent, 6) {
  override def getMaxStackSize: Int = 1
}
