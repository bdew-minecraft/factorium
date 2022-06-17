package net.bdew.factorium.metals

import net.bdew.factorium.worldgen.ores.{OreGenEnd, OreGenNether, OreGenOverworld}
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
    .withMeteoriteWeight(100)
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
    .withMeteoriteWeight(10)
    .addProcessing()
    .addGear()
    .addPlate()
    .addRod()
    .addWire()

  val gold: MetalEntry = MetalEntry("gold")
    .addVanillaItem(MetalItemType.Ingot, () => MCItems.GOLD_INGOT)
    .addVanillaItem(MetalItemType.Nugget, () => MCItems.GOLD_NUGGET)
    .addVanillaBlock(MetalItemType.StorageBlock, () => MCBlocks.GOLD_BLOCK)
    .addVanillaBlock(MetalItemType.OreNormal, () => MCBlocks.GOLD_ORE)
    .addVanillaBlock(MetalItemType.OreDeep, () => MCBlocks.DEEPSLATE_GOLD_ORE)
    .addVanillaItem(MetalItemType.RawDrop, () => MCItems.RAW_GOLD)
    .addVanillaBlock(MetalItemType.RawBlock, () => MCBlocks.RAW_GOLD_BLOCK)
    .addVanillaItem(MetalItemType.RawDrop, () => MCItems.RAW_GOLD)
    .withMeteoriteWeight(10)
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
    .withMeteoriteWeight(10)
    .addOreGen(OreGenOverworld("tin_overworld", _,
      defaultCount = 12,
      defaultMinY = -64,
      defaultMaxY = 100,
      defaultSize = 10,
      defaultAirExposure = 0))

  val lead: MetalEntry = MetalEntry("lead")
    .addResource()
    .addProcessing()
    .addPlate()
    .withMeteoriteWeight(10)
    .addOreGen(OreGenOverworld("lead_overworld", _,
      defaultCount = 8,
      defaultMinY = -64,
      defaultMaxY = 32,
      defaultSize = 5,
      defaultAirExposure = 0)
    )

  val nickel: MetalEntry = MetalEntry("nickel")
    .addResource()
    .addProcessing()
    .addPlate()
    .withMeteoriteWeight(50)
    .addOreGen(OreGenOverworld("nickel_overworld", _,
      defaultCount = 15,
      defaultMinY = -64,
      defaultMaxY = 64,
      defaultSize = 6,
      defaultAirExposure = 0))

  val zinc: MetalEntry = MetalEntry("zinc")
    .addResource()
    .addProcessing()
    .addPlate()
    .withMeteoriteWeight(10)
    .addOreGen(OreGenOverworld("zinc_overworld", _,
      defaultCount = 10,
      defaultMinY = -64,
      defaultMaxY = 128,
      defaultSize = 4,
      defaultAirExposure = 0))

  val silver: MetalEntry = MetalEntry("silver")
    .addResource()
    .addProcessing()
    .withMeteoriteWeight(10)
    .addOreGen(OreGenOverworld("silver_overworld", _,
      defaultCount = 10,
      defaultMinY = -64,
      defaultMaxY = 32,
      defaultSize = 4,
      defaultAirExposure = 0.5f))
    .addOreGen(OreGenEnd("silver_end", _,
      defaultCount = 4,
      defaultMinY = 0,
      defaultMaxY = 255,
      defaultSize = 10,
      defaultAirExposure = 0))

  val platinum: MetalEntry = MetalEntry("platinum")
    .addResource()
    .addProcessing()
    .addPlate()
    .addWire()
    .withMeteoriteWeight(10)
    .addOreGen(OreGenOverworld("platinum_overworld", _,
      defaultCount = 5,
      defaultMinY = -64,
      defaultMaxY = 20,
      defaultSize = 5,
      defaultAirExposure = 0.8f))
    .addOreGen(OreGenNether("platinum_nether", _,
      defaultCount = 6,
      defaultMinY = 0,
      defaultMaxY = 255,
      defaultSize = 8,
      defaultAirExposure = 0.5f))
    .addOreGen(OreGenEnd("platinum_end", _,
      defaultCount = 6,
      defaultMinY = 0,
      defaultMaxY = 255,
      defaultSize = 8,
      defaultAirExposure = 0))

  val bronze: MetalEntry = MetalEntry("bronze")
    .addResource()
    .addPlate()
    .addGear()
    .addRod()

  val steel: MetalEntry = MetalEntry("steel")
    .addResource()
    .addPlate()
    .addGear()
    .addRod()

  val invar: MetalEntry = MetalEntry("invar")
    .addResource()
    .addPlate()
    .addGear()
    .addRod()

  val electrum: MetalEntry = MetalEntry("electrum")
    .addResource()
    .addPlate()
    .addGear()
    .addRod()

  val constantan: MetalEntry = MetalEntry("constantan")
    .addResource()
    .addPlate()

  val brass: MetalEntry = MetalEntry("brass")
    .addResource()
    .addPlate()
    .addRod()

  val all: List[MetalEntry] = List(
    iron, copper, gold, tin, nickel, lead, silver, platinum, zinc,
    bronze, steel, invar, electrum, brass, constantan
  )

  def init(): Unit = {
    LogManager.getLogger.info(s"Registered ${all.size} metals")
  }
}
