package net.bdew.advtech.datagen

import net.minecraftforge.forge.event.lifecycle.GatherDataEvent

object DataGeneration {
  def onGatherData(ev: GatherDataEvent): Unit = {
    val dataGenerator = ev.getGenerator
    val efh = ev.getExistingFileHelper
    dataGenerator.addProvider(new LootTables(dataGenerator))
    dataGenerator.addProvider(new BlockStates(dataGenerator, efh))
    dataGenerator.addProvider(new ItemModels(dataGenerator, efh))
    val blockTags = new BlockTagsGen(dataGenerator, efh)
    dataGenerator.addProvider(blockTags)
    dataGenerator.addProvider(new ItemTagsGen(dataGenerator, efh, blockTags))
  }
}
