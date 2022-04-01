package net.bdew.factorium.machines.pump

import net.bdew.factorium.machines.BaseMachineBlock
import net.bdew.factorium.registries.BlockProps
import net.bdew.lib.capabilities.helpers.FluidHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}

class PumpBlock extends Block(BlockProps.machineProps) with BaseMachineBlock[PumpEntity] {

  override def use(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult = {
    if (!world.isClientSide) {
      if (FluidHelper.blockFluidInteract(world, pos, player, hand))
        return InteractionResult.CONSUME
    }
    super.use(state, world, pos, player, hand, hit)
  }
}
