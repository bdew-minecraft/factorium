package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.registries.{MetalItemType, Metals}
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

  override def addTags(): Unit = {
    copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS)
    copy(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR)
    copy(Tags.Blocks.ORE_RATES_SPARSE, Tags.Items.ORE_RATES_SPARSE)
    copy(Tags.Blocks.ORE_RATES_DENSE, Tags.Items.ORE_RATES_DENSE)
    copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE)
    copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE)
    copy(Tags.Blocks.ORES, Tags.Items.ORES)

    for (metal <- Metals.all) {
      val smeltableTag = ItemTags.createOptional(new ResourceLocation(AdvTech.ModId, s"smeltable/${metal.name}"))

      if (metal.registerIngot) {
        val ingot = metal.item(MetalItemType.Ingot)
        tag(Tags.Items.INGOTS).add(ingot)
        tag(forgeTagCustom(s"ingots/${metal.name}")).add(ingot)
      }

      if (metal.registerNugget) {
        val nugget = metal.item(MetalItemType.Nugget)
        tag(Tags.Items.NUGGETS).add(nugget)
        tag(forgeTagCustom(s"nuggets/${metal.name}")).add(nugget)
      }

      if (metal.registerOre) {
        val rawDrop = metal.item(MetalItemType.RawDrop)
        tag(Tags.Items.RAW_MATERIALS).add(rawDrop)
        tag(forgeTagCustom(s"raw_materials/${metal.name}")).add(rawDrop)
        copy(blockTags.forgeTagCustom("ores", metal.name), forgeTagCustom("ores", metal.name))
        copy(blockTags.forgeTagCustom("storage_blocks", s"raw_${metal.name}"), forgeTagCustom("storage_blocks", s"raw_${metal.name}"))
        tag(smeltableTag).add(metal.item(MetalItemType.OreNormal), metal.item(MetalItemType.OreDeep), rawDrop)
      }

      if (metal.registerBlock) {
        copy(blockTags.forgeTagCustom("storage_blocks", metal.name), forgeTagCustom("storage_blocks", metal.name))
      }

      if (metal.registerProcessing) {
        tag(smeltableTag).add(metal.item(MetalItemType.Chunks), metal.item(MetalItemType.Dust), metal.item(MetalItemType.Powder))
      }
    }
  }
}
