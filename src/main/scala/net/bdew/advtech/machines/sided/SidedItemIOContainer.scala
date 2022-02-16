package net.bdew.advtech.machines.sided

import net.bdew.lib.container.switchable.{ContainerMode, SwitchableContainer}

trait SidedItemIOContainer extends SwitchableContainer {
  def te: SidedItemIOEntity
  addMode(SidedItemIOContainer.SidedItemIOMode)
}

object SidedItemIOContainer {
  val SidedItemIOMode: ContainerMode = ContainerMode("sides_item_io")
}

