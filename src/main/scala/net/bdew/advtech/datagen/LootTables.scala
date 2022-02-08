package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.machines.BaseMachineBlock
import net.bdew.advtech.registries.Blocks
import net.bdew.lib.datagen.LootTableGenerator
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootTable

class LootTables(gen: DataGenerator) extends LootTableGenerator(gen, AdvTech.ModId) {
  override def makeTables(): Map[ResourceLocation, LootTable] = {
    Blocks.all.map(_.get() match {
      case block: BaseMachineBlock[_] => makeBlockEntry(block, makeKeepDataDropTable(block))
      case block => makeBlockEntry(block, makeSimpleDropTable(block))
    }).toMap
  }
}
