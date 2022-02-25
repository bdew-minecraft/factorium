package net.bdew.advtech.metals

case class MetalItemGroup(kind: String)
case class MetalItemType(kind: String, group: MetalItemGroup)

object MetalItemType {
  val groupResourceItem: MetalItemGroup = MetalItemGroup("resource")
  val groupStorageBlock: MetalItemGroup = MetalItemGroup("storage")
  val groupOreNormal: MetalItemGroup = MetalItemGroup("ore_normal")
  val groupOreDeep: MetalItemGroup = MetalItemGroup("ore_deep")
  val groupOreEnd: MetalItemGroup = MetalItemGroup("ore_end")
  val groupOreNether: MetalItemGroup = MetalItemGroup("ore_nether")

  val Ingot: MetalItemType = MetalItemType("ingot", groupResourceItem)
  val Nugget: MetalItemType = MetalItemType("nugget", groupResourceItem)
  val OreNormal: MetalItemType = MetalItemType("ore", groupOreNormal)
  val OreDeep: MetalItemType = MetalItemType("ore_deep", groupOreDeep)
  val OreEnd: MetalItemType = MetalItemType("ore_end", groupOreEnd)
  val OreNether: MetalItemType = MetalItemType("ore_nether", groupOreNether)
  val RawDrop: MetalItemType = MetalItemType("raw", groupResourceItem)
  val RawBlock: MetalItemType = MetalItemType("raw_block", groupStorageBlock)
  val StorageBlock: MetalItemType = MetalItemType("block", groupStorageBlock)
  val Gear: MetalItemType = MetalItemType("gear", groupResourceItem)
  val Plate: MetalItemType = MetalItemType("plate", groupResourceItem)
  val Rod: MetalItemType = MetalItemType("rod", groupResourceItem)
  val Wire: MetalItemType = MetalItemType("wire", groupResourceItem)
  val Dust: MetalItemType = MetalItemType("dust", groupResourceItem)
  val Chunks: MetalItemType = MetalItemType("chunks", groupResourceItem)
  val Powder: MetalItemType = MetalItemType("powder", groupResourceItem)

  val ores = Set(OreNormal, OreDeep, OreNether, OreEnd)
  val smeltables = Set(OreNormal, OreDeep, OreNether, OreEnd, RawDrop, Dust, Chunks, Powder)
}