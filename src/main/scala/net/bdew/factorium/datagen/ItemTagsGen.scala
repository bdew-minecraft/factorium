package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.metals.{MetalEntry, MetalItemType, Metals}
import net.bdew.factorium.registries.Items
import net.minecraft.data.DataGenerator
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{ItemTags, Tag}
import net.minecraft.world.item.Item
import net.minecraftforge.common.Tags
import net.minecraftforge.common.data.ExistingFileHelper

class ItemTagsGen(gen: DataGenerator, efh: ExistingFileHelper, blockTags: BlockTagsGen) extends ItemTagsProvider(gen, blockTags, Factorium.ModId, efh) {
  def addTypedForgeTag(metal: MetalEntry, kind: MetalItemType, groupTag: Tag.Named[Item]): Unit = {
    if (!metal.ownItem(kind)) return
    val item = metal.item(kind)
    val subTag = ItemTags.createOptional(new ResourceLocation(groupTag.getName.getNamespace, s"${groupTag.getName.getPath}/${metal.name}"))
    tag(groupTag).add(item)
    tag(subTag).add(item)
  }

  override def addTags(): Unit = {
    copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS)
    copy(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR)
    copy(Tags.Blocks.ORE_RATES_SPARSE, Tags.Items.ORE_RATES_SPARSE)
    copy(Tags.Blocks.ORE_RATES_DENSE, Tags.Items.ORE_RATES_DENSE)
    copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE)
    copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE)
    copy(Tags.Blocks.ORES, Tags.Items.ORES)

    copy(CustomTags.endStoneOres.block, CustomTags.endStoneOres.item)

    CustomTags.wrenchTags.foreach(t => tag(t).add(Items.wrench.get()))

    copy(CustomTags.ores("meteorite").block, CustomTags.ores("meteorite").item)

    for (metal <- Metals.all) {
      addTypedForgeTag(metal, MetalItemType.Ingot, Tags.Items.INGOTS)
      addTypedForgeTag(metal, MetalItemType.Nugget, Tags.Items.NUGGETS)
      addTypedForgeTag(metal, MetalItemType.RawDrop, Tags.Items.RAW_MATERIALS)
      addTypedForgeTag(metal, MetalItemType.Chunks, CustomTags.chunks)
      addTypedForgeTag(metal, MetalItemType.Powder, CustomTags.powders)
      addTypedForgeTag(metal, MetalItemType.Dust, Tags.Items.DUSTS)
      addTypedForgeTag(metal, MetalItemType.Rod, Tags.Items.RODS)
      addTypedForgeTag(metal, MetalItemType.Plate, CustomTags.plates)
      addTypedForgeTag(metal, MetalItemType.Gear, CustomTags.gears)
      addTypedForgeTag(metal, MetalItemType.Wire, CustomTags.wires)

      if (metal.ownItem(MetalItemType.Rod))
        tag(CustomTags.metalRods).add(metal.item(MetalItemType.Rod))

      if (metal.ownItem(MetalItemType.RawBlock))
        copy(CustomTags.storageBlock(s"raw_${metal.name}").block, CustomTags.storageBlock(s"raw_${metal.name}").item)

      if (MetalItemType.ores.exists(metal.ownItem))
        copy(CustomTags.ores(metal.name).block, CustomTags.ores(metal.name).item)

      if (metal.ownItem(MetalItemType.StorageBlock))
        copy(CustomTags.storageBlock(metal.name).block, CustomTags.storageBlock(metal.name).item)

      if (MetalItemType.smeltables.exists(metal.ownItem))
        tag(CustomTags.smeltable(metal.name)).add(MetalItemType.smeltables.filter(metal.ownItem).map(metal.item).toArray: _*)
    }

    for ((name, regObj) <- Items.extraDusts; item = regObj.get()) {
      tag(Tags.Items.DUSTS).add(item)
      tag(CustomTags.dusts(name)).add(item)
    }
  }
}
