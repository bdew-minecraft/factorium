package net.bdew.advtech.datagen

import net.bdew.advtech.AdvTech
import net.bdew.advtech.machines.RotatableMachineBlock
import net.bdew.advtech.registries.Blocks
import net.bdew.lib.datagen.BlockStateGenerator
import net.minecraft.core.Direction
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.ConfiguredModel
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.ForgeRegistryEntry

class BlockStates(gen: DataGenerator, efh: ExistingFileHelper) extends BlockStateGenerator(gen, AdvTech.ModId, efh) {
  def matTex(obj: ForgeRegistryEntry[_]) =
    "materials/" + obj.getRegistryName.getPath.substring(4).split("_", 2).mkString("/")

  def makeRotatableBlock(block: RotatableMachineBlock): Unit = {
    val model = models().getBuilder(block.getRegistryName.getPath)
      .parent(new ExistingModelFile(new ResourceLocation(AdvTech.ModId, "block/machine_rotatable"), efh))
      .texture("top", new ResourceLocation(AdvTech.ModId, "block/top"))
      .texture("front", new ResourceLocation(AdvTech.ModId, s"block/${block.getRegistryName.getPath}"))
      .texture("base", new ResourceLocation(AdvTech.ModId, "block/base"))

    getVariantBuilder(block).forAllStates(state => {
      block.getFacing(state) match {
        case Direction.NORTH =>
          ConfiguredModel.builder().modelFile(model).build()
        case Direction.EAST =>
          ConfiguredModel.builder().modelFile(model).rotationY(90).build()
        case Direction.SOUTH =>
          ConfiguredModel.builder().modelFile(model).rotationY(180).build()
        case Direction.WEST =>
          ConfiguredModel.builder().modelFile(model).rotationY(270).build()
        case x =>
          throw new IllegalArgumentException(s"Invalid facing $x")
      }
    })

    makeBlockItem(block, model)
  }

  def makeMaterialBlock(block: Block): Unit = {
    val model = models().getBuilder(block.getRegistryName.getPath)
      .parent(vanillaModel("block/cube_all"))
      .texture("all", new ResourceLocation(AdvTech.ModId, matTex(block)))
    genStates(block, _ => model)
    makeBlockItem(block, model)
  }

  override def registerStatesAndModels(): Unit = {
    Blocks.all.foreach(_.get() match {
      case x: RotatableMachineBlock => makeRotatableBlock(x)
      case x if x.getRegistryName.getPath.startsWith("mat_") => makeMaterialBlock(x)
      case x => makeBlock(x)
    })
  }
}
