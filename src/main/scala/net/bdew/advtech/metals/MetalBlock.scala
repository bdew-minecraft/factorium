package net.bdew.advtech.metals

import net.bdew.advtech.AdvTech
import net.bdew.lib.Text
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.{Block, OreBlock}

trait NamedBlock extends Block {
  def itemType: MetalItemType
  def metal: MetalEntry
  override lazy val getDescriptionId: String = s"block.${AdvTech.ModId}.metal.${itemType.kind}"
  override lazy val getName: MutableComponent = Text.translate(getDescriptionId, metal.displayName)
}

case class MetalBlock(props: BlockBehaviour.Properties, itemType: MetalItemType, metal: MetalEntry) extends Block(props) with NamedBlock
case class MetalOreBlock(props: BlockBehaviour.Properties, itemType: MetalItemType, metal: MetalEntry) extends OreBlock(props) with NamedBlock
