package net.bdew.factorium.blocks

import net.bdew.factorium.registries.BlockProps
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.{Block, ConcretePowderBlock}

class ReinforcedConcreteBlock(val color: DyeColor) extends Block(BlockProps.reinforcedConcreteProps(color)) with DyedBlock {
  override val baseName: String = "concrete.reinforced"
}

class GlowingConcreteBlock(val color: DyeColor) extends Block(BlockProps.glowingConcreteProps(color)) with DyedBlock {
  override val baseName: String = "concrete.glow"
}

class ReinforcedConcretePowderBlock(val color: DyeColor, concrete: ReinforcedConcreteBlock) extends ConcretePowderBlock(concrete, BlockProps.concretePowderProps(color)) with DyedBlock {
  override val baseName: String = "concrete.reinforced.powder"
}

class GlowingConcretePowderBlock(val color: DyeColor, concrete: GlowingConcreteBlock) extends ConcretePowderBlock(concrete, BlockProps.glowingConcretePowderProps(color)) with DyedBlock {
  override val baseName: String = "concrete.glow.powder"
}