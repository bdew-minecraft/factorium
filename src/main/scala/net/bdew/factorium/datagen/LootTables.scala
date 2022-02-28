package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.machines.BaseMachineBlock
import net.bdew.factorium.metals.{MetalItemType, MetalOreBlock, Metals}
import net.bdew.factorium.registries.Blocks
import net.bdew.lib.datagen.LootTableGenerator
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.OreBlock
import net.minecraft.world.level.storage.loot.LootTable

class LootTables(gen: DataGenerator) extends LootTableGenerator(gen, Factorium.ModId) {
  override def makeTables(): Map[ResourceLocation, LootTable] = {
    Blocks.all.map(_.get() match {
      case block: OreBlock if block.getRegistryName.getPath == "mat_meteorite_ore" =>
        makeBlockEntry(block, LootTableHelper.makeMeteoriteDropTable(block,
          Metals.all.filter(m => m.haveItem(MetalItemType.RawDrop) && m.meteoriteWeight > 0)
            .map(m => (m.item(MetalItemType.RawDrop), Int.box(m.meteoriteWeight)))
        ))
      case block: MetalOreBlock =>
        makeBlockEntry(block, LootTableHelper.makeOreDropTable(block, block.metal.item(MetalItemType.RawDrop)))
      case block: BaseMachineBlock[_] => makeBlockEntry(block, makeKeepDataDropTable(block))
      case block => makeBlockEntry(block, makeSimpleDropTable(block))
    }).toMap
  }
}
