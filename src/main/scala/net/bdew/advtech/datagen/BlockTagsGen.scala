package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.metals.{MetalItemType, Metals}
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
      for (kind <- MetalItemType.ores if metal.ownBlock(kind)) {
        val oreBlock = metal.block(kind)

        tag(Tags.Blocks.ORES).add(oreBlock)
        tag(Tags.Blocks.ORE_RATES_SINGULAR).add(oreBlock)
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(oreBlock)
        tag(BlockTags.NEEDS_STONE_TOOL).add(oreBlock)

        if (kind == MetalItemType.OreNormal)
          tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(oreBlock)
        if (kind == MetalItemType.OreDeep)
          tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(oreBlock)

        tag(forgeTagCustom("ores", metal.name)).add(oreBlock)
      }

      if (metal.ownBlock(MetalItemType.RawBlock)) {
        val rawBlock = metal.block(MetalItemType.RawBlock)
        tag(Tags.Blocks.STORAGE_BLOCKS).add(rawBlock)
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(rawBlock)
        tag(BlockTags.NEEDS_STONE_TOOL).add(rawBlock)
        tag(forgeTagCustom("storage_blocks", s"raw_${metal.name}")).add(rawBlock)
      }

      if (metal.ownBlock(MetalItemType.StorageBlock)) {
        val storageBlock = metal.block(MetalItemType.StorageBlock)
        tag(Tags.Blocks.STORAGE_BLOCKS).add(storageBlock)
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(storageBlock)
        tag(BlockTags.NEEDS_STONE_TOOL).add(storageBlock)
        tag(forgeTagCustom("storage_blocks", metal.name)).add(storageBlock)
      }
    }
  }
}
