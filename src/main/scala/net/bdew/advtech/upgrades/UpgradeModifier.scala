package net.bdew.advtech.upgrades

import net.bdew.lib.Text._
import net.minecraft.network.chat.MutableComponent

trait UpgradeModifier[T] {
  def stat: UpgradeStat[T]
  def calculate(initial: T): T
  def display: MutableComponent
}

case class UpgradeFloatMultiplier(stat: UpgradeStat[Float], multiplier: Float) extends UpgradeModifier[Float] {
  override def calculate(initial: Float): Float = initial * multiplier
  override def display: MutableComponent = stat.display & string(" ") & string("x %.0f".format(multiplier)).setColor(Color.YELLOW)
}

case class UpgradeFloatModifier(stat: UpgradeStat[Float], modifier: Float) extends UpgradeModifier[Float] {
  override def calculate(initial: Float): Float = initial * (1 + modifier)
  override def display: MutableComponent = stat.display & string(" ") &
    string("%s%.0f%%".format(if (modifier < 0) "" else "+", modifier * 100)).setColor(Color.YELLOW)
}

case class UpgradeIntSet(stat: UpgradeStat[Int], value: Int) extends UpgradeModifier[Int] {
  override def calculate(initial: Int): Int = value
  override def display: MutableComponent = stat.display & string(" ") &
    string("%d".format(value)).setColor(Color.YELLOW)
}