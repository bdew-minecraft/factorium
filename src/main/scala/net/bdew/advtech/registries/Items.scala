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
    if (metal.registerIngot) simple(metal.registryName(MetalItemType.Ingot), resourceProps)
    if (metal.registerGear) simple(metal.registryName(MetalItemType.Gear), resourceProps)
    if (metal.registerPlate) simple(metal.registryName(MetalItemType.Plate), resourceProps)
    if (metal.registerNugget) simple(metal.registryName(MetalItemType.Nugget), resourceProps)
    if (metal.registerOre) {
      simple(metal.registryName(MetalItemType.RawDrop), resourceProps)
    }
    if (metal.registerProcessing) {
      simple(metal.registryName(MetalItemType.Dust), resourceProps)
      simple(metal.registryName(MetalItemType.Chunks), resourceProps)
      simple(metal.registryName(MetalItemType.Powder), resourceProps)
    }
  }

  override def init(): Unit = {
    super.init()
    UpgradeItems.init()
  }
}
