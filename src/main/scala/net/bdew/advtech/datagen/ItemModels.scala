package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.registries.Items
import net.bdew.lib.datagen.ItemModelGenerator
import net.minecraft.data.DataGenerator
import net.minecraft.world.item.BlockItem
import net.minecraftforge.common.data.ExistingFileHelper

class ItemModels(gen: DataGenerator, efh: ExistingFileHelper) extends ItemModelGenerator(gen, AdvTech.ModId, efh) {
  override def registerModels(): Unit = {
    Items.all.foreach(_.get() match {
      case _: BlockItem => //skip
      case item if item.getRegistryName.getPath.startsWith("mat_") =>
        simpleItemModel(item, "materials/" + item.getRegistryName.getPath.substring(4).replace("_", "/"))
      case item => simpleItemModel(item, "item/" + item.getRegistryName.getPath.replace("_", "/"))
    })
  }
}
