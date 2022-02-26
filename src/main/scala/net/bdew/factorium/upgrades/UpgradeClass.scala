package net.bdew.factorium.upgrades

case class UpgradeClass(id: String)

object UpgradeClass {
  val Core: UpgradeClass = UpgradeClass("core")
  val SpeedEfficiency: UpgradeClass = UpgradeClass("speed_efficiency")
  val Parallel: UpgradeClass = UpgradeClass("parallel")
}
