package net.bdew.advtech.registries

import net.bdew.lib.managers.ItemManager
import net.minecraft.world.item.{CreativeModeTab, Item, ItemStack}

object CreativeTab extends CreativeModeTab("compacter") {
  override def makeIcon(): ItemStack = new ItemStack(Blocks.crusher.item.get())
}

object Items extends ItemManager(CreativeTab) {
  def resourceProps: Item.Properties = props

  for (metal <- Metals.all) {
    if (metal.registerIngot) simple(s"ingot_${metal.name}", resourceProps)
    if (metal.registerGear) simple(s"gear_${metal.name}", resourceProps)
    if (metal.registerPlate) simple(s"plate_${metal.name}", resourceProps)
    if (metal.registerProcessing) {
      simple(s"dust_${metal.name}", resourceProps)
      simple(s"crushed_${metal.name}", resourceProps)
      simple(s"pulverized_${metal.name}", resourceProps)
    }
  }

}
