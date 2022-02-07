package net.bdew.advtech.machines

import net.bdew.lib.data.base.TileDataSlots
import net.bdew.lib.keepdata.TileKeepData
import net.bdew.lib.tile.{TileExtended, TileTickingServer}
import net.minecraft.core.BlockPos
import net.minecraft.world.MenuProvider
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class BaseMachineEntity(teType: BlockEntityType[_], pos: BlockPos, state: BlockState)
  extends TileExtended(teType, pos, state)
    with TileDataSlots
    with TileKeepData
    with TileTickingServer
    with MenuProvider {

}
