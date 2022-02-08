package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.registries.Items
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.{BlockItem, Item}
import net.minecraftforge.client.model.generators.{ItemModelProvider, ModelFile}
import net.minecraftforge.common.data.ExistingFileHelper

abstract class ItemModels(gen: DataGenerator, modId: String, efh: ExistingFileHelper) extends ItemModelProvider(gen, modId, efh) {
  def simpleItemModel(item: Item, texture: String): Unit = {
    getBuilder(item.getRegistryName.getPath)
      .parent(new ModelFile.UncheckedModelFile("item/generated"))
      .texture("layer0", new ResourceLocation(modId, texture))
  }
}

class MyItemModels(gen: DataGenerator, efh: ExistingFileHelper) extends ItemModels(gen, AdvTech.ModId, efh) {
  override def registerModels(): Unit = {
    Items.all.foreach(_.get() match {
      case _: BlockItem => //skip
      case item if item.getRegistryName.getPath.startsWith("mat_") =>
        simpleItemModel(item, "materials/" + item.getRegistryName.getPath.substring(4).replace("_", "/"))
      case item => simpleItemModel(item, "item/" + item.getRegistryName.getPath.replace("_", "/"))
    })
  }
}
