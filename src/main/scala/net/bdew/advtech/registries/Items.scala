package net.bdew.advtech.registries

import net.bdew.advtech.items.WrenchItem
import net.bdew.advtech.metals.{MetalItem, MetalItemType, Metals}
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

  def resourceItems(prefix: String, names: String*): Map[String, RegistryObject[Item]] =
    names.map(id => id -> simple(s"${prefix}_$id", resourceProps)).toMap

  val wrench: RegistryObject[WrenchItem] = register("wrench", () => new WrenchItem())

  for (metal <- Metals.all; (kind, ref) <- metal.items if ref.isOwned) {
    kind.group match {
      case MetalItemType.groupResourceItem => register(metal.registryName(kind), () => MetalItem(resourceProps, kind, metal))
      case _ => // pass
    }
  }

  val extraDusts: Map[String, RegistryObject[Item]] =
    resourceItems(prefix = "mat_extra_dust",
      "coal",
      "charcoal",
      "carbon",
      "diamond",
      "emerald",
      "ender_pearl",
      "obsidian"
    )

  val craftingItems: Map[String, RegistryObject[Item]] =
    resourceItems(prefix = "craft",
      "coupler", "motor", "coil", "heater", "capacitor",
      "crusher", "grinder", "pulverizer",
    )

  override def init(): Unit = {
    super.init()
    UpgradeItems.init()
  }
}
