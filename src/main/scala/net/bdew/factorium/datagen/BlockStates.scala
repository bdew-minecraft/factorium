package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.machines.RotatableMachineBlock
import net.bdew.factorium.machines.pump.PumpBlock
import net.bdew.factorium.registries.Blocks
import net.bdew.lib.datagen.BlockStateGenerator
import net.minecraft.core.Direction
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.ConfiguredModel
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.ForgeRegistryEntry

class BlockStates(gen: DataGenerator, efh: ExistingFileHelper) extends BlockStateGenerator(gen, Factorium.ModId, efh) {
  def matTex(obj: ForgeRegistryEntry[_]): String =
    "materials/" + obj.getRegistryName.getPath.substring(4).split("_", 2).mkString("/")

  def makeRotatableBlock(block: RotatableMachineBlock): Unit = {
    val model = models().getBuilder(block.getRegistryName.getPath)
      .parent(new ExistingModelFile(new ResourceLocation(Factorium.ModId, "block/machine_rotatable"), efh))
      .texture("top", new ResourceLocation(Factorium.ModId, "block/top"))
      .texture("front", new ResourceLocation(Factorium.ModId, s"block/${block.getRegistryName.getPath}"))
      .texture("base", new ResourceLocation(Factorium.ModId, "block/base"))

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
      .texture("all", new ResourceLocation(Factorium.ModId, matTex(block)))
    genStates(block, _ => model)
    makeBlockItem(block, model)
  }

  def makeTopBottomBlock(block: Block): Unit = {
    val name = block.getRegistryName.getPath
    val model = models().getBuilder(name)
      .parent(vanillaModel("block/cube_bottom_top"))
      .texture("top", new ResourceLocation(Factorium.ModId, s"block/$name/top"))
      .texture("bottom", new ResourceLocation(Factorium.ModId, s"block/$name/bottom"))
      .texture("side", new ResourceLocation(Factorium.ModId, s"block/$name/side"))
    genStates(block, _ => model)
    makeBlockItem(block, model)
  }

  override def registerStatesAndModels(): Unit = {
    Blocks.all.foreach(_.get() match {
      case x: PumpBlock => makeTopBottomBlock(x)
      case x: RotatableMachineBlock => makeRotatableBlock(x)
      case x if x.getRegistryName.getPath.startsWith("mat_") => makeMaterialBlock(x)
      case x => makeBlock(x)
    })
  }
}
