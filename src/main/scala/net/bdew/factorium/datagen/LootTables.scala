package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.machines.BaseMachineBlock
import net.bdew.factorium.metals.{MetalEntry, MetalItemType, Metals}
import net.bdew.factorium.registries.Blocks
import net.bdew.lib.datagen.LootTableGenerator
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.{Block, OreBlock}
import net.minecraft.world.level.storage.loot.LootTable

class LootTables(gen: DataGenerator) extends LootTableGenerator(gen, Factorium.ModId) {
  override def makeTables(): Map[ResourceLocation, LootTable] = {
    val oreBlocksMap: Map[Block, MetalEntry] =
      Metals.all.flatMap(metal =>
        MetalItemType.ores.filter(metal.ownBlock).map(ore => metal.block(ore) -> metal)
      ).toMap

    Blocks.all.map(_.get() match {
      case block: OreBlock =>
        makeBlockEntry(block, LootTableHelper.makeOreDropTable(block, oreBlocksMap(block).item(MetalItemType.RawDrop)))
      case block: BaseMachineBlock[_] => makeBlockEntry(block, makeKeepDataDropTable(block))
      case block => makeBlockEntry(block, makeSimpleDropTable(block))
    }).toMap
  }
}
