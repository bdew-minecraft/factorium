package net.bdew.factorium.datagen;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import scala.Tuple2;
import scala.collection.immutable.List;

public class LootTableHelper {
    private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

    // Implemented in java because scala barfs at the type mess here

    public static LootTable.Builder makeOreDropTable(Block block, Item item) {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block)
                                .when(HAS_SILK_TOUCH)
                                .otherwise(
                                        LootItem.lootTableItem(item)
                                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                                .apply(ApplyExplosionDecay.explosionDecay())
                                )
                        )
        );
    }

    public static LootTable.Builder makeMeteoriteDropTable(Block block, List<Tuple2<ItemLike, Integer>> drops) {
        LootPool.Builder dropsPool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(5, 8))
                .add(
                        LootItem.lootTableItem(Items.DIAMOND)
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .setWeight(5)
                )
                .add(
                        LootItem.lootTableItem(Items.REDSTONE)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .setWeight(5)
                ).add(
                        LootItem.lootTableItem(Items.LAPIS_LAZULI)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .setWeight(5)
                );

        dropsPool = drops.foldLeft(dropsPool, (p, ent) ->
                p.add(
                        LootItem.lootTableItem(ent._1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .setWeight(ent._2)
                )
        );

        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block)
                                .when(HAS_SILK_TOUCH)
                        ))
                .withPool(dropsPool.when(InvertedLootItemCondition.invert(HAS_SILK_TOUCH)));
    }
}
