package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.items.ToolItem
import net.bdew.factorium.registries.Items
import net.bdew.lib.datagen.ItemModelGenerator
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.{BlockItem, Item}
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.ForgeRegistryEntry

class ItemModels(gen: DataGenerator, efh: ExistingFileHelper) extends ItemModelGenerator(gen, Factorium.ModId, efh) {
  def matTex(obj: ForgeRegistryEntry[_]): String =
    "materials/" + obj.getRegistryName.getPath.substring(4).split("_", 3).mkString("/")

  def toolItemModel(item: Item, texture: String): Unit = {
    getBuilder(item.getRegistryName.getPath)
      .parent(vanillaModel("item/handheld"))
      .texture("layer0", new ResourceLocation(Factorium.ModId, texture))
  }

  override def registerModels(): Unit = {
    Items.all.foreach(_.get() match {
      case _: BlockItem => //skip
      case x: ToolItem =>
        toolItemModel(x, s"item/${x.getRegistryName.getPath.replace("_", "/")}")
      case item if item.getRegistryName.getPath.startsWith("mat_") =>
        simpleItemModel(item, matTex(item))
      case item => simpleItemModel(item, "item/" + item.getRegistryName.getPath.replace("_", "/"))
    })
  }
}
