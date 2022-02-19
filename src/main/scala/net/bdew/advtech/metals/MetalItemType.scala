package net.bdew.advtech.metals

case class MetalItemGroup(kind: String)
case class MetalItemType(kind: String, group: MetalItemGroup)

object MetalItemType {
  val groupResourceItem: MetalItemGroup = MetalItemGroup("resource")
  val groupStorageBlock: MetalItemGroup = MetalItemGroup("storage")
  val groupOreNormal: MetalItemGroup = MetalItemGroup("ore_normal")
  val groupOreDeep: MetalItemGroup = MetalItemGroup("ore_deep")

  val Ingot: MetalItemType = MetalItemType("ingot", groupResourceItem)
  val Nugget: MetalItemType = MetalItemType("nugget", groupResourceItem)
  val OreNormal: MetalItemType = MetalItemType("ore", groupOreNormal)
  val OreDeep: MetalItemType = MetalItemType("ore_deep", groupOreDeep)
  val RawDrop: MetalItemType = MetalItemType("raw", groupResourceItem)
  val RawBlock: MetalItemType = MetalItemType("raw_block", groupStorageBlock)
  val StorageBlock: MetalItemType = MetalItemType("block", groupStorageBlock)
  val Gear: MetalItemType = MetalItemType("gear", groupResourceItem)
  val Plate: MetalItemType = MetalItemType("plate", groupResourceItem)
  val Rod: MetalItemType = MetalItemType("rod", groupResourceItem)
  val Dust: MetalItemType = MetalItemType("dust", groupResourceItem)
  val Chunks: MetalItemType = MetalItemType("chunks", groupResourceItem)
  val Powder: MetalItemType = MetalItemType("powder", groupResourceItem)

  val ores = Set(OreNormal, OreDeep)
  val smeltables = Set(OreNormal, OreDeep, RawDrop, Dust, Chunks, Powder)
}