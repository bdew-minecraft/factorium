package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.machines.BaseMachineBlock
import net.bdew.factorium.metals.{MetalEntry, MetalItemType, Metals}
import net.bdew.factorium.registries.Blocks
import net.bdew.lib.datagen.LootTableGenerator
import net.minecraft.advancements.critereon.{EnchantmentPredicate, ItemPredicate, MinMaxBounds}
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.{Block, OreBlock}
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.{ApplyBonusCount, ApplyExplosionDecay}
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.{LootPool, LootTable}

class LootTables(gen: DataGenerator) extends LootTableGenerator(gen, Factorium.ModId) {
  private val HAS_SILK_TOUCH = MatchTool.toolMatches(
    ItemPredicate.Builder.item.hasEnchantment(
      new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))
    )
  )

  def makeOreDropTable(block: Block, item: Item): LootTable.Builder = {
    LootTable.lootTable.withPool(
      LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1))
        .add(LootItem.lootTableItem(block)
          .when(HAS_SILK_TOUCH)
          .otherwise(
            LootItem.lootTableItem(item)
              .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
              .apply(ApplyExplosionDecay.explosionDecay)
          )
        )
    )
  }

  override def makeTables(): Map[ResourceLocation, LootTable] = {
    val oreBlocksMap: Map[Block, MetalEntry] =
      Metals.all.flatMap(metal =>
        MetalItemType.ores.filter(metal.ownBlock).map(ore => metal.block(ore) -> metal)
      ).toMap

    Blocks.all.map(_.get() match {
      case block: OreBlock =>
        makeBlockEntry(block, makeOreDropTable(block, oreBlocksMap(block).item(MetalItemType.RawDrop)))
      case block: BaseMachineBlock[_] => makeBlockEntry(block, makeKeepDataDropTable(block))
      case block => makeBlockEntry(block, makeSimpleDropTable(block))
    }).toMap
  }
}
