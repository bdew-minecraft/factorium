package net.bdew.factorium.datagen

import net.bdew.lib.datagen.LootTableSimpleGenerator
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraftforge.data.event.GatherDataEvent

object DataGeneration {
  def onGatherData(ev: GatherDataEvent): Unit = {
    val dataGenerator = ev.getGenerator
    val efh = ev.getExistingFileHelper
    val lookupFuture = ev.getLookupProvider
    dataGenerator.addProvider(ev.includeServer, new LootTableSimpleGenerator(_, LootContextParamSets.BLOCK, () => new BlockLootTablesSubProvider))
    dataGenerator.addProvider(ev.includeClient, new BlockStates(_, efh))
    dataGenerator.addProvider(ev.includeClient, new ItemModels(_, efh))
    val blockTags = dataGenerator.addProvider(ev.includeServer, new BlockTagsGen(_, efh, lookupFuture))
    dataGenerator.addProvider(ev.includeServer, new ItemTagsGen(_, efh, blockTags, lookupFuture))
    dataGenerator.addProvider(ev.includeServer, new RecipeGen(_))
    dataGenerator.addProvider(ev.includeServer, new WorldGenProvider(_, lookupFuture))
  }
}
