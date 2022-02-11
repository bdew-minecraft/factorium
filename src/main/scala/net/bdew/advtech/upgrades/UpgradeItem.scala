package net.bdew.advtech.upgrades

import net.bdew.advtech.registries.Items
import net.bdew.lib.Text._
import net.minecraft.network.chat.Component
import net.minecraft.world.item.{Item, ItemStack, TooltipFlag}
import net.minecraft.world.level.Level

import java.util

case class UpgradeItem(cls: UpgradeClass, modifiers: UpgradeModifier[_]*) extends Item(Items.props) {
  def calculate[T](stat: UpgradeStat[T], v: T): T =
    modifiers.filter(_.stat == stat).foldRight(v)((u, v) => u.asInstanceOf[UpgradeModifier[T]].calculate(v))

  override def appendHoverText(stack: ItemStack, level: Level, tooltip: util.List[Component], flags: TooltipFlag): Unit = {
    tooltip.add(translate("advtech.tooltip.modifiers").setColor(Color.YELLOW))
    modifiers.foreach(mod =>
      tooltip.add(string(" - ") & mod.display)
    )
  }
}

object UpgradeItems {
  def init(): Unit = {
    Items.register("upgrade_core_t1", () => UpgradeItem(UpgradeClass.Core,
      UpgradeFloatMultiplier(UpgradeStat.WorkSpeed, 2),
      UpgradeFloatMultiplier(UpgradeStat.EnergyConsumption, 2),
      UpgradeFloatMultiplier(UpgradeStat.EnergyCapacity, 2),
      UpgradeFloatMultiplier(UpgradeStat.ChargeRate, 2),
    ))

    Items.register("upgrade_efficiency", () => UpgradeItem(UpgradeClass.Efficiency,
      UpgradeFloatModifier(UpgradeStat.WorkSpeed, -0.3f),
      UpgradeFloatModifier(UpgradeStat.EnergyConsumption, -0.2f),
    ))

    Items.register("upgrade_speed", () => UpgradeItem(UpgradeClass.Speed,
      UpgradeFloatModifier(UpgradeStat.WorkSpeed, 0.2f),
      UpgradeFloatModifier(UpgradeStat.EnergyConsumption, 0.3f),
    ))

    Items.register("parallel_t1", () => UpgradeItem(UpgradeClass.Parallel,
      UpgradeIntSet(UpgradeStat.ParallelProcess, 2),
      UpgradeFloatModifier(UpgradeStat.EnergyConsumption, 0.5f),
    ))
  }
}