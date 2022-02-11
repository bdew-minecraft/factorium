package net.bdew.advtech.upgrades

import net.bdew.lib.Text
import net.minecraft.network.chat.{Component, TranslatableComponent}

case class InfoEntryKind(id: String) {
  def name: TranslatableComponent = Text.translate(s"advtech.info.${id}")
  def value(v: Component): Option[InfoEntry] = Some(InfoEntry(this, v))
}

object InfoEntryKind {
  val CycleLength: InfoEntryKind = InfoEntryKind("cycle_length")
  val EnergyUsed: InfoEntryKind = InfoEntryKind("energy_used")
  val ItemsPerCycle: InfoEntryKind = InfoEntryKind("items_per_cycle")
}

case class InfoEntry(kind: InfoEntryKind, text: Component)

trait InfoSource {
  def statsDisplay(line: Int): Option[InfoEntry]
}