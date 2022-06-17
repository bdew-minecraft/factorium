package net.bdew.factorium.datagen

import net.minecraftforge.forge.event.lifecycle.GatherDataEvent

object DataGeneration {
  def onGatherData(ev: GatherDataEvent): Unit = {
    val dataGenerator = ev.getGenerator
    val efh = ev.getExistingFileHelper
    dataGenerator.addProvider(ev.includeServer, new LootTables(dataGenerator))
    dataGenerator.addProvider(ev.includeServer, new BlockStates(dataGenerator, efh))
    dataGenerator.addProvider(ev.includeServer, new ItemModels(dataGenerator, efh))
    val blockTags = new BlockTagsGen(dataGenerator, efh)
    dataGenerator.addProvider(ev.includeServer, blockTags)
    dataGenerator.addProvider(ev.includeServer, new ItemTagsGen(dataGenerator, efh, blockTags))
    dataGenerator.addProvider(ev.includeServer, new RecipeGen(dataGenerator))

    val worldGen = new WorldGenProviders(dataGenerator, efh)

    dataGenerator.addProvider(ev.includeServer, worldGen.configuredFeatureProvider)
    dataGenerator.addProvider(ev.includeServer, worldGen.placedFeatureProvider)
    dataGenerator.addProvider(ev.includeServer, worldGen.biomeModifiersProvider)
  }
}
