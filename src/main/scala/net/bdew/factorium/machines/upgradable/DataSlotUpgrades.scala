package net.bdew.factorium.machines.upgradable

import net.bdew.factorium.upgrades.{UpgradeClass, UpgradeItem, UpgradeStat}
import net.bdew.lib.Misc
import net.bdew.lib.data.DataSlotInventory
import net.minecraft.world.item.ItemStack

class DataSlotUpgrades(parent: UpgradeableMachine) extends DataSlotInventory("upgrades", parent, 6) {
  override def getMaxStackSize: Int = 1
  var needsUpdate = true

  override def canPlaceItem(slot: Int, stack: ItemStack): Boolean =
    stack.getItem match {
      case upgrade: UpgradeItem =>
        upgrade.cls == UpgradeClass.None || !upgrades.exists(_.cls == upgrade.cls)
      case _ => false
    }

  def upgrades: List[UpgradeItem] = inv.toList.filter(stack => !stack.isEmpty)
    .flatMap(stack => Misc.asInstanceOpt(stack.getItem, classOf[UpgradeItem]))

  def calculate[T](stat: UpgradeStat[T], initial: T): T =
    inv.filter(stack => !stack.isEmpty)
      .flatMap(stack => Misc.asInstanceOpt(stack.getItem, classOf[UpgradeItem]))
      .foldLeft(initial)((value, item) => item.calculate(stat, value))

  override def setChanged(): Unit = {
    super.setChanged()
    needsUpdate = true
  }
}
