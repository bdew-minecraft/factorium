package net.bdew.factorium.machines.worker

import net.bdew.factorium.machines.{BaseMachineBlock, RotatableMachineBlock}
import net.bdew.factorium.registries.BlockProps
import net.bdew.lib.block.StatefulBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class WorkerMachineBlock[E <: WorkerMachineEntity] extends StatefulBlock(BlockProps.machineProps) with BaseMachineBlock[E] with RotatableMachineBlock {
  override def neighborChanged(state: BlockState, world: Level, pos: BlockPos, block: Block, fromPos: BlockPos, moving: Boolean): Unit = {
    val te = getTE(world, pos)
    if (!te.haveWork && !world.isClientSide && te.canWorkRS) {
      te.haveWork = true
    }
  }
}
