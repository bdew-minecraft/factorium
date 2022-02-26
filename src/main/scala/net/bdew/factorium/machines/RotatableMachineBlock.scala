package net.bdew.factorium.machines

import net.bdew.lib.rotate.StatefulBlockFacing
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.properties.{BlockStateProperties, DirectionProperty}

import java.util

trait RotatableMachineBlock extends StatefulBlockFacing {
  override def facingProperty: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING

  override val getValidFacings: util.EnumSet[Direction] =
    util.EnumSet.of(Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH)

  override def getDefaultFacing: Direction = Direction.NORTH
}
