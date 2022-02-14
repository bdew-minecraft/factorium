package net.bdew.advtech.machines

import net.bdew.lib.block.HasTETickingServer
import net.bdew.lib.keepdata.BlockKeepData
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}
import net.minecraftforge.network.NetworkHooks

trait BaseMachineBlock[E <: BaseMachineEntity] extends Block with BlockKeepData with HasTETickingServer[E] {
  override def use(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult = {
    if (world.isClientSide) {
      InteractionResult.SUCCESS
    } else player match {
      case serverPlayer: ServerPlayer =>
        NetworkHooks.openGui(serverPlayer, getTE(world, pos), pos)
        InteractionResult.CONSUME
      case _ => InteractionResult.FAIL
    }
  }
}
