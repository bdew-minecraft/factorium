package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.registries.{MetalItemType, Metals}
import net.minecraft.data.DataGenerator
import net.minecraft.data.tags.BlockTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{BlockTags, Tag}
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.Tags
import net.minecraftforge.common.data.ExistingFileHelper

class BlockTagsGen(gen: DataGenerator, efh: ExistingFileHelper) extends BlockTagsProvider(gen, AdvTech.ModId, efh) {
  def forgeTagCustom(name: String*): Tag.Named[Block] =
    BlockTags.createOptional(new ResourceLocation("forge", name.mkString("/")))

  override def addTags(): Unit = {
    for (metal <- Metals.all) {
      if (metal.registerOre) {
        val oreNormal = metal.block(MetalItemType.OreNormal)
        val oreDeep = metal.block(MetalItemType.OreDeep)
        val rawBlock = metal.block(MetalItemType.RawBlock)

        tag(Tags.Blocks.ORES).add(oreNormal, oreDeep)
        tag(Tags.Blocks.ORE_RATES_SINGULAR).add(oreNormal, oreDeep)
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(oreNormal, oreDeep, rawBlock)
        tag(BlockTags.NEEDS_STONE_TOOL).add(oreNormal, oreDeep, rawBlock)

        tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(oreNormal)
        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(oreDeep)

        tag(forgeTagCustom("ores", metal.name)).add(oreNormal, oreDeep)

        tag(Tags.Blocks.STORAGE_BLOCKS).add(rawBlock)
        tag(forgeTagCustom("storage_blocks", s"raw_${metal.name}")).add(rawBlock)
      }

      if (metal.registerBlock) {
        val storageBlock = metal.block(MetalItemType.StorageBlock)

        tag(Tags.Blocks.STORAGE_BLOCKS).add(storageBlock)
        tag(forgeTagCustom("storage_blocks", metal.name)).add(storageBlock)
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(storageBlock)
        tag(BlockTags.NEEDS_STONE_TOOL).add(storageBlock)
      }
    }
  }
}
