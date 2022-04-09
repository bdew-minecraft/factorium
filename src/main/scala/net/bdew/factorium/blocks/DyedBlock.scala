package net.bdew.factorium.blocks

import net.bdew.factorium.Factorium
import net.bdew.factorium.registries.Items
import net.bdew.lib.Text
import net.minecraft.network.chat.{MutableComponent, TranslatableComponent}
import net.minecraft.world.item.{BlockItem, DyeColor, ItemStack}
import net.minecraft.world.level.block.Block

trait DyedBlock extends Block {
  def color: DyeColor
  def baseName: String
  lazy val colorName: TranslatableComponent = Text.translate(s"color.minecraft.${color.getName}")
  override lazy val getDescriptionId: String = s"block.${Factorium.ModId}.dyed.${baseName}"
  override lazy val getName: MutableComponent = Text.translate(getDescriptionId, colorName)
}

case class DyedBlockItem(block: DyedBlock) extends BlockItem(block, Items.resourceProps) {
  override def getName(stack: ItemStack): MutableComponent = block.getName
}
