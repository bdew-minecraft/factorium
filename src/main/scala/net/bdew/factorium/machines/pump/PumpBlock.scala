package net.bdew.factorium.machines.pump

import net.bdew.factorium.machines.BaseMachineBlock
import net.bdew.factorium.registries.Blocks
import net.bdew.lib.capabilities.Capabilities
import net.bdew.lib.capabilities.helpers.FluidHelper
import net.bdew.lib.items.ItemUtils
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}
import net.minecraftforge.fluids.FluidStack

class PumpBlock extends Block(Blocks.machineProps) with BaseMachineBlock[PumpEntity] {

  override def use(state: BlockState, world: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult = {
    if (!world.isClientSide) {
      val item = player.getItemInHand(hand)
      if (!item.isEmpty) {
        val cont = item.copy()
        cont.setCount(1)
        val didSomething = cont.getCapability(Capabilities.CAP_FLUID_HANDLER_ITEM).map[Boolean](cap => {
          val moved = getTE(world, pos).getCapability(Capabilities.CAP_FLUID_HANDLER).map[FluidStack](ourCap => {
            FluidHelper.pushFluid(ourCap, cap)
          }).orElse(FluidStack.EMPTY)
          if (moved.isEmpty) {
            false
          } else {
            world.playSound(null, pos, SoundEvents.BUCKET_FILL, player.getSoundSource, 1, 1)
            if (item.getCount == 1)
              player.setItemInHand(hand, cap.getContainer)
            else {
              item.shrink(1)
              ItemUtils.dropItemToPlayer(world, player, cap.getContainer)
            }
            true
          }
        }).orElse(false)
        if (didSomething) return InteractionResult.CONSUME
      }
    }
    super.use(state, world, pos, player, hand, hit)
  }
}
