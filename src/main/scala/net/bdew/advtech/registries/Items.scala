package net.bdew.advtech.registries

import net.bdew.advtech.items.WrenchItem
import net.bdew.advtech.metals.{MetalItemType, Metals}
import net.bdew.advtech.upgrades.UpgradeItems
import net.bdew.lib.managers.ItemManager
import net.minecraft.world.item.{CreativeModeTab, Item, ItemStack}
import net.minecraftforge.registries.RegistryObject

object CreativeTab extends CreativeModeTab("advtech") {
  override def makeIcon(): ItemStack = new ItemStack(Blocks.crusher.item.get())
}

object Items extends ItemManager(CreativeTab) {
  def resourceProps: Item.Properties = props
  def toolProps: Item.Properties = props.stacksTo(1)

  val wrench: RegistryObject[WrenchItem] = register("wrench", () => new WrenchItem())

  for (metal <- Metals.all; (item, ref) <- metal.items if ref.isOwned) {
    item.group match {
      case MetalItemType.groupResourceItem => simple(metal.registryName(item), resourceProps)
      case _ => // pass
    }
  }

  override def init(): Unit = {
    super.init()
    UpgradeItems.init()
  }
}
