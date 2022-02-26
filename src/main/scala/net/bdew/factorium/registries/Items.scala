package net.bdew.factorium.registries

import net.bdew.factorium.items.WrenchItem
import net.bdew.factorium.metals.{MetalItem, MetalItemType, Metals}
import net.bdew.factorium.upgrades.UpgradeItems
import net.bdew.lib.managers.ItemManager
import net.minecraft.world.item.{CreativeModeTab, Item, ItemStack}
import net.minecraftforge.registries.RegistryObject

object CreativeTab extends CreativeModeTab("factorium") {
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

  simple("upgrade_frame", resourceProps)

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

  resourceItems(prefix = "craft",
    "coupler", "motor", "coil", "heater", "capacitor",
    "quartz_pulse", "quartz_clock", "container",
    "core_basic", "core_advanced",
    "crusher", "grinder", "pulverizer",
  )

  override def init(): Unit = {
    super.init()
    UpgradeItems.init()
  }
}
