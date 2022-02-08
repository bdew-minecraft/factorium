package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.registries.Blocks
import net.bdew.lib.datagen.BlockStateGenerator
import net.minecraft.data.DataGenerator
import net.minecraftforge.common.data.ExistingFileHelper

class BlockStates(gen: DataGenerator, efh: ExistingFileHelper) extends BlockStateGenerator(gen, AdvTech.ModId, efh) {
  override def registerStatesAndModels(): Unit = {
    Blocks.all.foreach(_.get() match {
      case x => makeBlock(x)
    })
  }
}
