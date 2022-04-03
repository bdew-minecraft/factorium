package net.bdew.factorium.registries

import net.bdew.factorium.items.{FuelItem, WrenchItem}
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
      "diamond",
      "emerald",
      "ender_pearl",
      "obsidian",
      "sand",
      "quicklime",
      "silica"
    ) ++ Map(
      "coal" -> register("mat_extra_dust_coal", () => new FuelItem(1600)),
      "charcoal" -> register("mat_extra_dust_charcoal", () => new FuelItem(1600)),
      "carbon" -> register("mat_carbon_dust", () => new FuelItem(3200)),
    )

  val extraChunks: Map[String, RegistryObject[Item]] =
    resourceItems(prefix = "mat_extra_chunks",
      "calcite"
    )

  val resin: RegistryObject[Item] = simple("mat_resin", resourceProps)

  simple("mat_wood_pulp", resourceProps)
  simple("mat_carbon_wet", resourceProps)
  simple("mat_carbon_electrode", resourceProps)
  simple("mat_carbon_fiber", resourceProps)

  register("mat_wood_chips", () => new FuelItem(1600))

  resourceItems(prefix = "craft",
    "coupler", "motor", "coil", "heater", "capacitor",
    "quartz_pulse", "quartz_clock", "container", "tank",
    "core_basic", "core_advanced",
    "crusher", "grinder", "pulverizer", "compressor",
    "mesh_carbon", "mesh_reinforced",
  )

  val dies: Map[String, RegistryObject[Item]] = resourceItems(prefix = "die",
    "plate", "gear", "rod", "wire", "nugget",
  )

  override def init(): Unit = {
    super.init()
    UpgradeItems.init()
  }
}
