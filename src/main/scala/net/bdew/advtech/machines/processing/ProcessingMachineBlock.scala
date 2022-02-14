package net.bdew.advtech.machines.processing

import net.bdew.advtech.machines.{BaseMachineBlock, RotatableMachineBlock}
import net.bdew.advtech.registries.Blocks
import net.bdew.lib.block.StatefulBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class ProcessingMachineBlock[E <: ProcessingMachineEntity] extends StatefulBlock(Blocks.machineProps) with BaseMachineBlock[E] with RotatableMachineBlock {
  override def neighborChanged(state: BlockState, world: Level, pos: BlockPos, block: Block, fromPos: BlockPos, moving: Boolean): Unit = {
    val te = getTE(world, pos)
    if (!te.haveWork && !world.isClientSide && te.canWorkRS) {
      te.haveWork = true
    }
  }
}