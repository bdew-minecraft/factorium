package net.bdew.factorium.upgrades

import net.bdew.lib.Text
import net.minecraft.network.chat.MutableComponent

case class UpgradeStat[T](id: String) {
  def display: MutableComponent = Text.translate(s"factorium.stat.$id")
}

object UpgradeStat {
  val WorkSpeed: UpgradeStat[Float] = UpgradeStat("work_speed")
  val EnergyConsumption: UpgradeStat[Float] = UpgradeStat("energy_consumption")
  val ChargeRate: UpgradeStat[Float] = UpgradeStat("charge_rate")
  val EnergyCapacity: UpgradeStat[Float] = UpgradeStat("energy_capacity")
  val ParallelProcess: UpgradeStat[Int] = UpgradeStat("parallel_process")
}