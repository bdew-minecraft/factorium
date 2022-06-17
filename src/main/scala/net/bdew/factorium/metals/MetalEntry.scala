package net.bdew.factorium.metals

import net.bdew.factorium.Factorium
import net.bdew.factorium.worldgen.WorldgenTemplate
import net.bdew.lib.Text
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraftforge.registries.ForgeRegistries

trait Reference[+T] {
  def get: T
  def isOwned: Boolean
}

case class OwnItem(id: ResourceLocation) extends Reference[Item] {
  override def get: Item = ForgeRegistries.ITEMS.getValue(id)
  override def isOwned: Boolean = true
}

case class OwnBlock(id: ResourceLocation) extends Reference[Block] {
  override def get: Block = ForgeRegistries.BLOCKS.getValue(id)
  override def isOwned: Boolean = true
}

case class External[+T](getter: () => T) extends Reference[T] {
  override def get: T = getter()
  override def isOwned: Boolean = false
}

case class MetalEntry(name: String) {
  var items: Map[MetalItemType, Reference[Item]] = Map.empty
  var blocks: Map[MetalItemType, Reference[Block]] = Map.empty
  var oreGen: List[WorldgenTemplate] = List.empty
  var meteoriteWeight = 0

  def addItem(kind: MetalItemType, ref: Reference[Item]): MetalEntry = {
    items += kind -> ref
    this
  }

  def addBlock(kind: MetalItemType, ref: Reference[Block]): MetalEntry = {
    blocks += kind -> ref
    this
  }

  def addOwnItem(kind: MetalItemType): MetalEntry = {
    addItem(kind, OwnItem(registryKey(kind)))
  }

  def addOwnBlock(kind: MetalItemType): MetalEntry = {
    addItem(kind, OwnItem(registryKey(kind)))
    addBlock(kind, OwnBlock(registryKey(kind)))
  }

  def addOre(kind: MetalItemType): Reference[Block] = {
    if (!haveItem(MetalItemType.RawDrop)) addOwnItem(MetalItemType.RawDrop)
    if (!haveBlock(MetalItemType.RawBlock)) addOwnBlock(MetalItemType.RawBlock)
    blocks.get(kind) match {
      case Some(o) => o
      case None =>
        addOwnBlock(kind)
        blocks(kind)
    }
  }

  def addOreGen(factory: MetalEntry => WorldgenTemplate): MetalEntry = {
    oreGen :+= factory(this)
    this
  }

  def addProcessing(): MetalEntry =
    addOwnItem(MetalItemType.Chunks)
      .addOwnItem(MetalItemType.Powder)
      .addOwnItem(MetalItemType.Dust)

  def addResource(): MetalEntry =
    addOwnItem(MetalItemType.Ingot)
      .addOwnItem(MetalItemType.Nugget)
      .addOwnBlock(MetalItemType.StorageBlock)

  def addPlate(): MetalEntry =
    addOwnItem(MetalItemType.Plate)

  def addRod(): MetalEntry =
    addOwnItem(MetalItemType.Rod)

  def addGear(): MetalEntry =
    addOwnItem(MetalItemType.Gear)

  def addWire(): MetalEntry =
    addOwnItem(MetalItemType.Wire)

  def addVanillaItem(kind: MetalItemType, getter: () => Item): MetalEntry =
    addItem(kind, External(getter))

  def addVanillaBlock(kind: MetalItemType, getter: () => Block): MetalEntry =
    addBlock(kind, External(getter))

  def withMeteoriteWeight(w: Int): MetalEntry = {
    meteoriteWeight = w
    this
  }

  def haveItem(kind: MetalItemType): Boolean = items.isDefinedAt(kind)
  def haveBlock(kind: MetalItemType): Boolean = blocks.isDefinedAt(kind)

  def ownItem(kind: MetalItemType): Boolean = items.get(kind).exists(_.isOwned)
  def ownBlock(kind: MetalItemType): Boolean = blocks.get(kind).exists(_.isOwned)

  def registryName(kind: MetalItemType) = s"mat_${name}_${kind.kind}"
  def registryKey(kind: MetalItemType) = new ResourceLocation(Factorium.ModId, registryName(kind))

  def item(kind: MetalItemType): Item =
    items.getOrElse(kind, throw new RuntimeException(s"No $kind registered for $name")).get

  def block(kind: MetalItemType): Block =
    blocks.getOrElse(kind, throw new RuntimeException(s"No $kind registered for $name")).get

  lazy val displayName: MutableComponent = Text.translate(s"${Factorium.ModId}.metal.$name")
}