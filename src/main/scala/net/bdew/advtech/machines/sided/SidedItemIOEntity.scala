package net.bdew.advtech.machines.sided

import net.bdew.advtech.machines.BaseMachineEntity
import net.minecraft.core.Direction

trait SidedItemIOEntity extends BaseMachineEntity {
  def itemIOConfig: DataSlotSidedIOConfig
  def getFacing: Direction
}
