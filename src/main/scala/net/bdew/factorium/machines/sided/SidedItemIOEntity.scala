package net.bdew.factorium.machines.sided

import net.bdew.factorium.machines.BaseMachineEntity
import net.bdew.factorium.misc.AutoIOMode
import net.bdew.lib.capabilities.helpers.ItemHelper
import net.minecraft.core.Direction

import scala.util.Random

trait SidedItemIOEntity extends BaseMachineEntity {
  val itemIOConfig: DataSlotSidedIOConfig = DataSlotSidedIOConfig("itemIoSides", this)
  def getFacing: Direction

  private var ticker = Random.nextInt(40)

  serverTick.listen { () =>
    if (ticker <= 0) {
      for {
        side <- BlockSide.values
        cfg = itemIOConfig.get(side) if cfg != AutoIOMode.NONE
        dir = BlockSide.toDirection(getFacing, side)
        pos = getBlockPos.relative(dir) if getLevel.isLoaded(pos)
        otherHandler <- ItemHelper.getItemHandler(getLevel, pos, dir.getOpposite)
        ourHandler <- ItemHelper.getItemHandler(getLevel, getBlockPos, null)
      } {
        if (AutoIOMode.canInput(cfg)) ItemHelper.pushItems(otherHandler, ourHandler)
        if (AutoIOMode.canOutput(cfg)) ItemHelper.pushItems(ourHandler, otherHandler)
      }
      ticker = 40
    } else {
      ticker -= 1
    }
  }
}
