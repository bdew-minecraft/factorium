package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.registries.Blocks
import net.bdew.lib.datagen.BlockStateGenerator
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.data.ExistingFileHelper

class BlockStates(gen: DataGenerator, efh: ExistingFileHelper) extends BlockStateGenerator(gen, AdvTech.ModId, efh) {
  def makeMaterialBlock(block: Block): Unit = {
    val model = models().getBuilder(block.getRegistryName.getPath)
      .parent(vanillaModel("block/cube_all"))
      .texture("all", new ResourceLocation(AdvTech.ModId, "materials/" + block.getRegistryName.getPath.substring(4).replace("_", "/")))
    genStates(block, _ => model)
    makeBlockItem(block, model)
  }

  override def registerStatesAndModels(): Unit = {
    Blocks.all.foreach(_.get() match {
      case x if x.getRegistryName.getPath.startsWith("mat_") => makeMaterialBlock(x)
      case x => makeBlock(x)
    })
  }
}
