package net.bdew.advtech.upgrades

case class UpgradeClass(id: String)

object UpgradeClass {
  val None: UpgradeClass = UpgradeClass("*")
  val Core: UpgradeClass = UpgradeClass("core")
  val Efficiency: UpgradeClass = UpgradeClass("efficiency")
  val Speed: UpgradeClass = UpgradeClass("speed")
}
