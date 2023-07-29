package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{BlockTags, ItemTags, TagKey}
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object CustomTags {
  case class Duo(item: TagKey[Item], block: TagKey[Block])

  def tagItem(mod: String, name: String*): TagKey[Item] =
    ItemTags.create(new ResourceLocation(mod, name.mkString("/")))

  def tagBlock(mod: String, name: String*): TagKey[Block] =
    BlockTags.create(new ResourceLocation(mod, name.mkString("/")))

  def tagDual(mod: String, name: String*): Duo = Duo(
    ItemTags.create(new ResourceLocation(mod, name.mkString("/"))),
    BlockTags.create(new ResourceLocation(mod, name.mkString("/")))
  )

  lazy val chunks: TagKey[Item] = tagItem(Factorium.ModId, "chunks")
  lazy val powders: TagKey[Item] = tagItem(Factorium.ModId, "powders")

  def chunks(name: String): TagKey[Item] = tagItem(Factorium.ModId, "chunks", name)

  def powders(name: String): TagKey[Item] = tagItem(Factorium.ModId, "powders", name)

  lazy val plates: TagKey[Item] = tagItem("forge", "plates")
  def plates(name: String): TagKey[Item] = tagItem("forge", "plates", name)

  lazy val gears: TagKey[Item] = tagItem("forge", "gears")
  lazy val wires: TagKey[Item] = tagItem("forge", "wires")

  lazy val metalRods: TagKey[Item] = tagItem("forge", "rods", "all_metal")

  lazy val wrenchTags: Set[TagKey[Item]] = Set(
    tagItem("forge", "wrenches"),
    tagItem("forge", "tools", "wrench")
  )

  lazy val endStoneOres: Duo = tagDual("forge", "ores_in_ground", "end_stone")

  def storageBlock(name: String): Duo = tagDual("forge", "storage_blocks", name)

  def ores(name: String): Duo = tagDual("forge", "ores", name)

  def dusts(name: String): TagKey[Item] = tagItem("forge", "dusts", name)

  def ingots(name: String): TagKey[Item] = tagItem("forge", "ingots", name)

  lazy val nuggets: TagKey[Item] = tagItem("forge", "nuggets")
  def nuggets(name: String): TagKey[Item] = tagItem("forge", "nuggets", name)

  def smeltable(name: String): TagKey[Item] = tagItem(Factorium.ModId, "smeltable", name)

  def rawMaterials(name: String): TagKey[Item] = tagItem("forge", "raw_materials", name)

  def flowers(name: String): TagKey[Item] = tagItem("minecraft", "flowers", name)

  def dyes(color: String): TagKey[Item] = tagItem("forge", "dyes", color)
}
