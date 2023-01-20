package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.metals.{MetalEntry, MetalItemType, Metals}
import net.bdew.factorium.registries.Items
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.{ItemTags, TagKey}
import net.minecraft.world.item.{Item, Items => MCItems}
import net.minecraftforge.common.Tags
import net.minecraftforge.common.data.ExistingFileHelper

import java.util.concurrent.CompletableFuture

class ItemTagsGen(out: PackOutput, efh: ExistingFileHelper, blockTags: BlockTagsGen, lookUp: CompletableFuture[HolderLookup.Provider]) extends ItemTagsProvider(out, lookUp, blockTags, Factorium.ModId, efh) {
  def addTypedForgeTag(metal: MetalEntry, kind: MetalItemType, groupTag: TagKey[Item]): Unit = {
    if (!metal.ownItem(kind)) return
    val item = metal.item(kind)
    val subTag = ItemTags.create(new ResourceLocation(groupTag.location.getNamespace, s"${groupTag.location.getPath}/${metal.name}"))
    tag(groupTag).add(item)
    tag(subTag).add(item)
  }

  override def addTags(provider: HolderLookup.Provider): Unit = {
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

    for ((name, regObj) <- Items.extraChunks; item = regObj.get()) {
      tag(CustomTags.chunks).add(item)
      tag(CustomTags.chunks(name)).add(item)
    }

    tag(CustomTags.flowers("white")).add(MCItems.LILY_OF_THE_VALLEY)
    tag(CustomTags.flowers("orange")).add(MCItems.ORANGE_TULIP)
    tag(CustomTags.flowers("magenta")).add(MCItems.ALLIUM, MCItems.LILAC)
    tag(CustomTags.flowers("light_blue")).add(MCItems.BLUE_ORCHID)
    tag(CustomTags.flowers("yellow")).add(MCItems.DANDELION, MCItems.SUNFLOWER)
    tag(CustomTags.flowers("pink")).add(MCItems.PINK_TULIP, MCItems.PEONY)
    tag(CustomTags.flowers("light_gray")).add(MCItems.OXEYE_DAISY, MCItems.WHITE_TULIP, MCItems.AZURE_BLUET)
    tag(CustomTags.flowers("blue")).add(MCItems.CORNFLOWER)
    tag(CustomTags.flowers("brown")).add(MCItems.COCOA_BEANS)
    tag(CustomTags.flowers("red")).add(MCItems.ROSE_BUSH, MCItems.RED_TULIP, MCItems.POPPY)
    tag(CustomTags.flowers("black")).add(MCItems.WITHER_ROSE)

    tag(Tags.Items.SLIMEBALLS).add(Items.resin.get())
  }
}
