package net.bdew.advtech.metals

import net.bdew.advtech.worldgen.OreGenOverworld
import net.minecraft.world.item.{Items => MCItems}
import net.minecraft.world.level.block.{Blocks => MCBlocks}
import org.apache.logging.log4j.LogManager

object Metals {
  val iron: MetalEntry = MetalEntry("iron")
    .addVanillaItem(MetalItemType.Ingot, () => MCItems.IRON_INGOT)
    .addVanillaItem(MetalItemType.Nugget, () => MCItems.IRON_NUGGET)
    .addVanillaBlock(MetalItemType.StorageBlock, () => MCBlocks.IRON_BLOCK)
    .addVanillaBlock(MetalItemType.OreNormal, () => MCBlocks.IRON_ORE)
    .addVanillaBlock(MetalItemType.OreDeep, () => MCBlocks.DEEPSLATE_IRON_ORE)
    .addVanillaItem(MetalItemType.RawDrop, () => MCItems.RAW_IRON)
    .addVanillaBlock(MetalItemType.RawBlock, () => MCBlocks.RAW_IRON_BLOCK)
    .addVanillaItem(MetalItemType.RawDrop, () => MCItems.RAW_IRON)
    .addProcessing()
    .addGear()
    .addPlate()
    .addRod()

  val copper: MetalEntry = MetalEntry("copper")
    .addVanillaItem(MetalItemType.Ingot, () => MCItems.COPPER_INGOT)
    .addVanillaBlock(MetalItemType.StorageBlock, () => MCBlocks.COPPER_BLOCK)
    .addVanillaBlock(MetalItemType.OreNormal, () => MCBlocks.COPPER_ORE)
    .addVanillaBlock(MetalItemType.OreDeep, () => MCBlocks.DEEPSLATE_COPPER_ORE)
    .addVanillaItem(MetalItemType.RawDrop, () => MCItems.RAW_COPPER)
    .addVanillaBlock(MetalItemType.RawBlock, () => MCBlocks.RAW_COPPER_BLOCK)
    .addVanillaItem(MetalItemType.RawDrop, () => MCItems.RAW_COPPER)
    .addOwnItem(MetalItemType.Nugget)
    .addProcessing()
    .addGear()
    .addPlate()
    .addRod()

  val gold: MetalEntry = MetalEntry("gold")
    .addVanillaItem(MetalItemType.Ingot, () => MCItems.GOLD_INGOT)
    .addVanillaItem(MetalItemType.Nugget, () => MCItems.GOLD_NUGGET)
    .addVanillaBlock(MetalItemType.StorageBlock, () => MCBlocks.GOLD_BLOCK)
    .addVanillaBlock(MetalItemType.OreNormal, () => MCBlocks.GOLD_ORE)
    .addVanillaBlock(MetalItemType.OreDeep, () => MCBlocks.DEEPSLATE_GOLD_ORE)
    .addVanillaItem(MetalItemType.RawDrop, () => MCItems.RAW_GOLD)
    .addVanillaBlock(MetalItemType.RawBlock, () => MCBlocks.RAW_GOLD_BLOCK)
    .addVanillaItem(MetalItemType.RawDrop, () => MCItems.RAW_GOLD)
    .addProcessing()
    .addGear()
    .addPlate()
    .addRod()


  val tin: MetalEntry = MetalEntry("tin")
    .addResource()
    .addProcessing()
    .addPlate()
    .addGear()
    .addRod()
    .addOreGen(OreGenOverworld("tin_overworld", _,
      defaultCount = 20,
      defaultMinY = -30,
      defaultMaxY = 100,
      defaultSize = 10)
    )

  val bronze: MetalEntry = MetalEntry("bronze")
    .addResource()
    .addPlate()
    .addGear()
    .addRod()

  val all: List[MetalEntry] = List(iron, copper, gold, tin, bronze)

  def init(): Unit = {
    LogManager.getLogger.info(s"Registered ${all.size} metals")
  }
}
