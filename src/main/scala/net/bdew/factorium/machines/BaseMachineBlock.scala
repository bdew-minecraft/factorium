package net.bdew.factorium.machines

import net.bdew.lib.block.{HasTETickingServer, WrenchableBlock}
import net.bdew.lib.items.ItemUtils
import net.bdew.lib.keepdata.BlockKeepData
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.network.NetworkHooks

trait BaseMachineBlock[E <: BaseMachineEntity] extends Block with BlockKeepData with HasTETickingServer[E] {
  def dismantle(state: BlockState, world: Level, pos: BlockPos, serverPlayer: ServerPlayer): Boolean = {
    if (ForgeHooks.onBlockBreakEvent(world, serverPlayer.gameMode.getGameModeForPlayer, serverPlayer, pos) == -1
      || serverPlayer.blockActionRestricted(world, pos, serverPlayer.gameMode.getGameModeForPlayer))
      return false

    val soundType = state.getSoundType(world, pos, serverPlayer)
    val stack = new ItemStack(this)
    getTE(world, pos).saveDataToItem(stack)
    val removed = state.onDestroyedByPlayer(world, pos, serverPlayer, true, world.getFluidState(pos))
    if (removed) {
      world.playSound(null, pos, soundType.getBreakSound, SoundSource.BLOCKS, soundType.getVolume * 0.75f, soundType.getPitch * 1.2F)
      destroy(world, pos, state)
      ItemUtils.dropItemToPlayer(world, serverPlayer, stack)
    }
    true
  }

  override def use(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult = {
    if (world.isClientSide) {
      InteractionResult.SUCCESS
    } else player match {
      case serverPlayer: ServerPlayer =>
        if (serverPlayer.isSecondaryUseActive && WrenchableBlock.isWrench(player.getItemInHand(hand))) {
          if (dismantle(state, world, pos, serverPlayer))
            InteractionResult.CONSUME
          else
            InteractionResult.FAIL
        } else {
          NetworkHooks.openScreen(serverPlayer, getTE(world, pos), pos)
        }
        InteractionResult.CONSUME
      case _ => InteractionResult.FAIL
    }
  }
}
