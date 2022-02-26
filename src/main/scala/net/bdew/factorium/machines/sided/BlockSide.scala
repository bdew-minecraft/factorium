package net.bdew.factorium.machines.sided

import net.minecraft.core.Direction

object BlockSide extends Enumeration {
  val TOP: Value = Value(0, "Top")
  val BOTTOM: Value = Value(1, "Bottom")
  val LEFT: Value = Value(2, "Left")
  val RIGHT: Value = Value(3, "Right")
  val FRONT: Value = Value(4, "Front")
  val BACK: Value = Value(5, "Back")

  def toDirection(facing: Direction, side: BlockSide.Value): Direction = {
    if (facing.getAxis == Direction.Axis.Y) {
      side match {
        case TOP => Direction.NORTH
        case BOTTOM => Direction.SOUTH
        case RIGHT => Direction.EAST
        case LEFT => Direction.WEST
        case FRONT => facing
        case BACK => facing.getOpposite
      }
    } else {
      side match {
        case TOP => Direction.UP
        case BOTTOM => Direction.DOWN
        case FRONT => facing
        case BACK => facing.getOpposite
        case RIGHT => facing.getCounterClockWise(Direction.Axis.Y)
        case LEFT => facing.getClockWise(Direction.Axis.Y)
      }
    }
  }
}
