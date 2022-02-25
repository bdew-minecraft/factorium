package net.bdew.advtech.metals

import net.bdew.advtech.AdvTech
import net.bdew.lib.Text
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.{BlockItem, Item, ItemStack}

trait NamedItem extends Item {
  def itemType: MetalItemType
  def metal: MetalEntry
  override lazy val getDescriptionId: String = s"item.${AdvTech.ModId}.metal.${itemType.kind}"
  override def getName(stack: ItemStack): MutableComponent = Text.translate(getDescriptionId, metal.displayName)
}

case class MetalItem(props: Item.Properties, itemType: MetalItemType, metal: MetalEntry) extends Item(props) with NamedItem

case class MetalBlockItem(block: NamedBlock, props: Item.Properties) extends BlockItem(block, props) {
  override def getName(stack: ItemStack): MutableComponent = block.getName
}
