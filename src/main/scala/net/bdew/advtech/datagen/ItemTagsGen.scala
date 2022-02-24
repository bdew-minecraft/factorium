package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.metals.{MetalEntry, MetalItemType, Metals}
import net.bdew.advtech.registries.Items
import net.minecraft.data.DataGenerator
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{ItemTags, Tag}
import net.minecraft.world.item.Item
import net.minecraftforge.common.Tags
import net.minecraftforge.common.data.ExistingFileHelper

class ItemTagsGen(gen: DataGenerator, efh: ExistingFileHelper, blockTags: BlockTagsGen) extends ItemTagsProvider(gen, blockTags, AdvTech.ModId, efh) {
  def forgeTagCustom(name: String*): Tag.Named[Item] =
    ItemTags.createOptional(new ResourceLocation("forge", name.mkString("/")))

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

    tag(forgeTagCustom("wrenches")).add(Items.wrench.get())
    tag(forgeTagCustom("tools", "wrench")).add(Items.wrench.get())

    val chunksTag = ItemTags.createOptional(new ResourceLocation(AdvTech.ModId, "chunks"))
    val powdersTag = ItemTags.createOptional(new ResourceLocation(AdvTech.ModId, "powders"))

    val platesTag = forgeTagCustom("plates")
    val gearsTag = forgeTagCustom("gears")
    val wiresTag = forgeTagCustom("wires")
    val metalRodsTag = forgeTagCustom("rods", "all_metal")

    for (metal <- Metals.all) {
      addTypedForgeTag(metal, MetalItemType.Ingot, Tags.Items.INGOTS)
      addTypedForgeTag(metal, MetalItemType.Nugget, Tags.Items.NUGGETS)
      addTypedForgeTag(metal, MetalItemType.RawDrop, Tags.Items.RAW_MATERIALS)
      addTypedForgeTag(metal, MetalItemType.Chunks, chunksTag)
      addTypedForgeTag(metal, MetalItemType.Powder, powdersTag)
      addTypedForgeTag(metal, MetalItemType.Dust, Tags.Items.DUSTS)
      addTypedForgeTag(metal, MetalItemType.Rod, Tags.Items.RODS)
      addTypedForgeTag(metal, MetalItemType.Plate, platesTag)
      addTypedForgeTag(metal, MetalItemType.Gear, gearsTag)
      addTypedForgeTag(metal, MetalItemType.Wire, wiresTag)

      if (metal.ownItem(MetalItemType.Rod)) {
        tag(metalRodsTag).add(metal.item(MetalItemType.Rod))
      }

      if (metal.ownItem(MetalItemType.RawBlock)) {
        copy(blockTags.forgeTagCustom("storage_blocks", s"raw_${metal.name}"), forgeTagCustom("storage_blocks", s"raw_${metal.name}"))
      }

      if (MetalItemType.ores.exists(metal.ownItem)) {
        copy(blockTags.forgeTagCustom("ores", metal.name), forgeTagCustom("ores", metal.name))
      }

      if (metal.ownItem(MetalItemType.StorageBlock)) {
        copy(blockTags.forgeTagCustom("storage_blocks", metal.name), forgeTagCustom("storage_blocks", metal.name))
      }

      if (MetalItemType.smeltables.exists(metal.ownItem)) {
        val smeltableTag = ItemTags.createOptional(new ResourceLocation(AdvTech.ModId, s"smeltable/${metal.name}"))
        tag(smeltableTag).add(MetalItemType.smeltables.filter(metal.ownItem).map(metal.item).toArray: _*)
      }
    }

    for ((name, regObj) <- Items.extraDusts; item = regObj.get()) {
      tag(Tags.Items.DUSTS).add(item)
      tag(forgeTagCustom("dusts", name)).add(item)
    }
  }
}
