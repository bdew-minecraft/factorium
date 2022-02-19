package net.bdew.advtech.machines.worker

import net.bdew.advtech.machines.{BaseMachineBlock, RotatableMachineBlock}
import net.bdew.advtech.registries.Blocks
import net.bdew.lib.block.StatefulBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class WorkerMachineBlock[E <: WorkerMachineEntity] extends StatefulBlock(Blocks.machineProps) with BaseMachineBlock[E] with RotatableMachineBlock {
  override def neighborChanged(state: BlockState, world: Level, pos: BlockPos, block: Block, fromPos: BlockPos, moving: Boolean): Unit = {
    val te = getTE(world, pos)
    if (!te.haveWork && !world.isClientSide && te.canWorkRS) {
      te.haveWork = true
    }
  }
}
