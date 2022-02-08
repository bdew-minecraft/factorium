package net.bdew.advtech.registries

import net.bdew.advtech.upgrades.UpgradeItems
import net.bdew.lib.managers.ItemManager
import net.minecraft.world.item.{CreativeModeTab, Item, ItemStack}

object CreativeTab extends CreativeModeTab("compacter") {
  override def makeIcon(): ItemStack = new ItemStack(Blocks.crusher.item.get())
}

object Items extends ItemManager(CreativeTab) {
  def resourceProps: Item.Properties = props

  for (metal <- Metals.all) {
    if (metal.registerIngot) simple(s"mat_${metal.name}_ingot", resourceProps)
    if (metal.registerGear) simple(s"mat_${metal.name}_gear", resourceProps)
    if (metal.registerPlate) simple(s"mat_${metal.name}_plate", resourceProps)
    if (metal.registerNugget) simple(s"mat_${metal.name}_nugget", resourceProps)
    if (metal.registerProcessing) {
      simple(s"mat_${metal.name}_dust", resourceProps)
      simple(s"mat_${metal.name}_chunks", resourceProps)
      simple(s"mat_${metal.name}_powder", resourceProps)
    }
  }

  override def init(): Unit = {
    super.init()
    UpgradeItems.init()
  }
}
