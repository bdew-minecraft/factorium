package net.bdew.factorium.items

import net.bdew.factorium.registries.Items
import net.bdew.lib.Text
import net.minecraft.ChatFormatting
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.item.{Item, ItemStack, TooltipFlag}
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.{BlockStateProperties, ChestType}
import net.minecraft.world.level.block.{Block, Rotation}
import net.minecraft.world.level.{Level, LevelReader}

import java.util

class WrenchItem extends Item(Items.toolProps) with ToolItem {
  def checkAndSetState(world: Level, pos: BlockPos, prev: BlockState, state: BlockState, entity: Player): Boolean = {
    if (prev != state && state.canSurvive(world, pos)) {
      world.setBlockAndUpdate(pos, state)
      if (state.isSignalSource) {
        world.updateNeighborsAt(pos, state.getBlock)
        for (face <- Direction.values() if prev.getDirectSignal(world, pos, face) != state.getDirectSignal(world, pos, face))
          world.updateNeighborsAt(pos.relative(face), state.getBlock)
      }
      val soundType = state.getSoundType(world, pos, entity)
      world.playSound(entity, pos, soundType.getPlaceSound, SoundSource.BLOCKS, soundType.getVolume * 0.75f, soundType.getPitch)
      true
    } else false
  }

  override def doesSneakBypassUse(stack: ItemStack, world: LevelReader, pos: BlockPos, player: Player): Boolean =
    true

  override def onItemUseFirst(stack: ItemStack, ctx: UseOnContext): InteractionResult = {
    if (ctx.getPlayer.isSecondaryUseActive) return InteractionResult.PASS

    if (!ctx.getPlayer.mayUseItemAt(ctx.getClickedPos, ctx.getClickedFace, ctx.getItemInHand))
      return InteractionResult.PASS

    val original = ctx.getLevel.getBlockState(ctx.getClickedPos)

    if (original.getDestroySpeed(ctx.getLevel, ctx.getClickedPos) == Block.INDESTRUCTIBLE
      || original.hasProperty(BlockStateProperties.DOOR_HINGE)
      || original.hasProperty(BlockStateProperties.BED_PART)
      || (original.hasProperty(BlockStateProperties.CHEST_TYPE) && original.getValue(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE)
      || (original.hasProperty(BlockStateProperties.EXTENDED) && original.getValue(BlockStateProperties.EXTENDED))
    ) return InteractionResult.PASS

    if (original.hasProperty(BlockStateProperties.FACING)) {
      val modified = original.setValue(BlockStateProperties.FACING, ctx.getClickedFace)
      if (checkAndSetState(ctx.getLevel, ctx.getClickedPos, original, modified, ctx.getPlayer))
        return InteractionResult.sidedSuccess(ctx.getLevel.isClientSide)
    } else if (original.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && ctx.getClickedFace.getAxis.isHorizontal) {
      val modified = original.setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getClickedFace)
      if (checkAndSetState(ctx.getLevel, ctx.getClickedPos, original, modified, ctx.getPlayer))
        return InteractionResult.sidedSuccess(ctx.getLevel.isClientSide)
    } else if (original.hasProperty(BlockStateProperties.FACING_HOPPER)) {
      val newDir = if (ctx.getClickedFace.getAxis.isVertical) Direction.DOWN else ctx.getClickedFace
      val modified = original.setValue(BlockStateProperties.FACING_HOPPER, newDir)
      if (checkAndSetState(ctx.getLevel, ctx.getClickedPos, original, modified, ctx.getPlayer))
        return InteractionResult.sidedSuccess(ctx.getLevel.isClientSide)
    } else if (original.hasProperty(BlockStateProperties.AXIS)) {
      val modified = original.setValue(BlockStateProperties.AXIS, ctx.getClickedFace.getAxis)
      if (checkAndSetState(ctx.getLevel, ctx.getClickedPos, original, modified, ctx.getPlayer))
        return InteractionResult.sidedSuccess(ctx.getLevel.isClientSide)
    } else {
      val rotated = original.getBlock.rotate(original, ctx.getLevel, ctx.getClickedPos, Rotation.CLOCKWISE_90)
      if (checkAndSetState(ctx.getLevel, ctx.getClickedPos, original, rotated, ctx.getPlayer))
        return InteractionResult.sidedSuccess(ctx.getLevel.isClientSide)
    }

    super.useOn(ctx)
  }

  override def appendHoverText(stack: ItemStack, world: Level, tip: util.List[Component], flag: TooltipFlag): Unit = {
    super.appendHoverText(stack, world, tip, flag)
    tip.add(Text.translate(getDescriptionId + ".desc").withStyle(ChatFormatting.GRAY))
    tip.add(Text.translate(getDescriptionId + ".desc2").withStyle(ChatFormatting.GRAY))
  }
}
