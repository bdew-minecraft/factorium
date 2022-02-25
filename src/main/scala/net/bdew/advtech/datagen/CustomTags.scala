package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{BlockTags, ItemTags, Tag}
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object CustomTags {
  case class Duo(item: Tag.Named[Item], block: Tag.Named[Block])

  def tagItem(mod: String, name: String*): Tag.Named[Item] =
    ItemTags.createOptional(new ResourceLocation(mod, name.mkString("/")))

  def tagBlock(mod: String, name: String*): Tag.Named[Block] =
    BlockTags.createOptional(new ResourceLocation(mod, name.mkString("/")))

  def tagDual(mod: String, name: String*): Duo = Duo(
    ItemTags.createOptional(new ResourceLocation(mod, name.mkString("/"))),
    BlockTags.createOptional(new ResourceLocation(mod, name.mkString("/")))
  )

  lazy val chunks: Tag.Named[Item] = tagItem(AdvTech.ModId, "chunks")
  lazy val powders: Tag.Named[Item] = tagItem(AdvTech.ModId, "powders")

  lazy val plates: Tag.Named[Item] = tagItem("forge", "plates")
  lazy val gears: Tag.Named[Item] = tagItem("forge", "gears")
  lazy val wires: Tag.Named[Item] = tagItem("forge", "wires")

  lazy val metalRods: Tag.Named[Item] = tagItem("forge", "rods", "all_metal")

  lazy val wrenchTags: Set[Tag.Named[Item]] = Set(
    tagItem("forge", "wrenches"),
    tagItem("forge", "tools", "wrench")
  )

  lazy val endStoneOres: Duo = tagDual("forge", "ores_in_ground", "end_stone")

  def storageBlock(name: String): Duo = tagDual("forge", "storage_blocks", name)
  def ores(name: String): Duo = tagDual("forge", "ores", name)

  def dusts(name: String): Tag.Named[Item] = tagItem("forge", "dusts", name)
  def smeltable(name: String): Tag.Named[Item] = tagItem(AdvTech.ModId, "smeltable", name)
}
