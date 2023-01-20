package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.items.ToolItem
import net.bdew.factorium.registries.Items
import net.bdew.lib.datagen.ItemModelGenerator
import net.bdew.lib.misc.Taggable
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.{BlockItem, Item}
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.ForgeRegistries

class ItemModels(out: PackOutput, efh: ExistingFileHelper) extends ItemModelGenerator(out, Factorium.ModId, efh) {
  def matTex[T: Taggable](obj: T): String =
    "materials/" + Taggable[T].registry.getKey(obj).getPath.substring(4).split("_", 3).mkString("/")


  def toolItemModel(item: Item, texture: String): Unit = {
    getBuilder(ForgeRegistries.ITEMS.getKey(item).getPath)
      .parent(vanillaModel("item/handheld"))
      .texture("layer0", new ResourceLocation(Factorium.ModId, texture))
  }

  override def registerModels(): Unit = {
    Items.all.foreach(_.get() match {
      case _: BlockItem => //skip
      case x: ToolItem =>
        toolItemModel(x, s"item/${ForgeRegistries.ITEMS.getKey(x).getPath.replace("_", "/")}")
      case item if ForgeRegistries.ITEMS.getKey(item).getPath.startsWith("mat_") =>
        simpleItemModel(item, matTex(item))
      case item => simpleItemModel(item, "item/" + ForgeRegistries.ITEMS.getKey(item).getPath.replace("_", "/"))
    })
  }
}
