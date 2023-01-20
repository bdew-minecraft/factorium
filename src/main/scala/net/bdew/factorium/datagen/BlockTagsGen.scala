package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.metals.{MetalItemType, Metals}
import net.bdew.factorium.registries.Blocks
import net.bdew.factorium.worldgen.ores.OreGenMeteorite
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.{Blocks => MCBlocks}
import net.minecraftforge.common.Tags
import net.minecraftforge.common.data.{BlockTagsProvider, ExistingFileHelper}

import java.util.concurrent.CompletableFuture

class BlockTagsGen(gen: PackOutput, efh: ExistingFileHelper, lookUp: CompletableFuture[HolderLookup.Provider]) extends BlockTagsProvider(gen, lookUp, Factorium.ModId, efh) {

  override def addTags(provider: HolderLookup.Provider): Unit = {
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
        if (kind == MetalItemType.OreNether)
          tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(oreBlock)
        if (kind == MetalItemType.OreEnd)
          tag(CustomTags.endStoneOres.block).add(oreBlock)

        tag(CustomTags.ores(metal.name).block).add(oreBlock)
      }

      if (metal.ownBlock(MetalItemType.RawBlock)) {
        val rawBlock = metal.block(MetalItemType.RawBlock)
        tag(Tags.Blocks.STORAGE_BLOCKS).add(rawBlock)
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(rawBlock)
        tag(BlockTags.NEEDS_STONE_TOOL).add(rawBlock)
        tag(CustomTags.storageBlock(s"raw_${metal.name}").block).add(rawBlock)
      }

      if (metal.ownBlock(MetalItemType.StorageBlock)) {
        val storageBlock = metal.block(MetalItemType.StorageBlock)
        tag(Tags.Blocks.STORAGE_BLOCKS).add(storageBlock)
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(storageBlock)
        tag(BlockTags.NEEDS_STONE_TOOL).add(storageBlock)
        tag(CustomTags.storageBlock(metal.name).block).add(storageBlock)
      }
    }

    val meteoriteOre = Blocks.meteoriteOre.block.get()
    tag(Tags.Blocks.ORES).add(meteoriteOre)
    tag(Tags.Blocks.ORE_RATES_DENSE).add(meteoriteOre)
    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(meteoriteOre)
    tag(BlockTags.NEEDS_DIAMOND_TOOL).add(meteoriteOre)
    tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(meteoriteOre)
    tag(CustomTags.ores("meteorite").block).add(meteoriteOre)

    tag(OreGenMeteorite.replaceables).addTag(BlockTags.STONE_ORE_REPLACEABLES)
      .add(MCBlocks.SAND, MCBlocks.DIRT, MCBlocks.SANDSTONE, MCBlocks.GRAVEL)

    val reinforced = List(Blocks.reinforcedGlass.block.get()) ++ Blocks.reinforcedConcrete.values.map(_.get())

    reinforced foreach { block =>
      tag(BlockTags.DRAGON_IMMUNE).add(block)
      tag(BlockTags.WITHER_IMMUNE).add(block)
      tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block)
      tag(BlockTags.NEEDS_DIAMOND_TOOL).add(block)
    }

    Blocks.glowingConcrete.map(_._2.get()) foreach { block =>
      tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block)
    }

    Blocks.glowingConcretePowder.map(_._2.get()) foreach { block =>
      tag(BlockTags.MINEABLE_WITH_SHOVEL).add(block)
    }

    Blocks.reinforcedConcretePowder.map(_._2.get()) foreach { block =>
      tag(BlockTags.MINEABLE_WITH_SHOVEL).add(block)
    }
  }
}
