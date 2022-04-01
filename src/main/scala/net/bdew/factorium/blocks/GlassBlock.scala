package net.bdew.factorium.blocks

import net.bdew.factorium.registries.BlockProps
import net.bdew.lib.render.connected.ConnectedTextureBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.block.{GlassBlock => MCGlassBlock}

class GlassBlock(props: BlockBehaviour.Properties) extends MCGlassBlock(props) with ConnectedTextureBlock {
  override def canConnect(world: BlockGetter, origin: BlockPos, target: BlockPos): Boolean =
    world.getBlockState(target).is(this)
}


class GlassBlockDark extends GlassBlock(BlockProps.glassDarkProps) {
  override def propagatesSkylightDown(state: BlockState, world: BlockGetter, pos: BlockPos): Boolean = false
  override def getLightBlock(p_60585_ : BlockState, p_60586_ : BlockGetter, p_60587_ : BlockPos): Int = 15
}
