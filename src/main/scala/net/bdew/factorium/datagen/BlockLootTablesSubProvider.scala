package net.bdew.factorium.datagen

import net.bdew.factorium.machines.BaseMachineBlock
import net.bdew.factorium.metals.{MetalItemType, MetalOreBlock, Metals}
import net.bdew.factorium.registries.Blocks
import net.bdew.lib.datagen.LootTableUtils
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootTable

import java.util.function.BiConsumer

class BlockLootTablesSubProvider extends LootTableSubProvider {
  override def generate(consumer: BiConsumer[ResourceLocation, LootTable.Builder]): Unit = {
    Blocks.all.foreach(_.get() match {
      case block if block == Blocks.meteoriteOre.block.get() =>
        LootTableUtils.addBlockEntry(block, LootTableHelper.makeMeteoriteDropTable(block,
          Metals.all.filter(m => m.haveItem(MetalItemType.RawDrop) && m.meteoriteWeight > 0)
            .map(m => (m.item(MetalItemType.RawDrop), Int.box(m.meteoriteWeight)))
        ), consumer)
      case block: MetalOreBlock =>
        LootTableUtils.addBlockEntry(block, LootTableHelper.makeOreDropTable(block, block.metal.item(MetalItemType.RawDrop)), consumer)
      case block: BaseMachineBlock[_] =>
        LootTableUtils.addBlockEntry(block, LootTableUtils.makeKeepDataDropTable(block), consumer)
      case block =>
        LootTableUtils.addBlockEntry(block, LootTableUtils.makeSimpleDropTable(block), consumer)
    })
  }
}
