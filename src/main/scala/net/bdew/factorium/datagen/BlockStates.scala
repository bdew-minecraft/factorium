package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.machines.RotatableMachineBlock
import net.bdew.factorium.machines.pump.PumpBlock
import net.bdew.factorium.registries.Blocks
import net.bdew.lib.datagen.BlockStateGenerator
import net.bdew.lib.misc.Taggable
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.ConfiguredModel
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.ForgeRegistries

class BlockStates(out: PackOutput, efh: ExistingFileHelper) extends BlockStateGenerator(out, Factorium.ModId, efh) {
  def matTex[T: Taggable](obj: T): String =
    "materials/" + Taggable[T].registry.getKey(obj).getPath.substring(4).split("_", 2).mkString("/")

  def concreteTex[T: Taggable](obj: T): String =
    "block/" + Taggable[T].registry.getKey(obj).getPath.split("_", 4).mkString("/")

  def makeRotatableBlock(block: RotatableMachineBlock): Unit = {
    val model = models().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath)
      .parent(new ExistingModelFile(new ResourceLocation(Factorium.ModId, "block/machine_rotatable"), efh))
      .texture("top", new ResourceLocation(Factorium.ModId, "block/top"))
      .texture("front", new ResourceLocation(Factorium.ModId, s"block/${ForgeRegistries.BLOCKS.getKey(block).getPath}"))
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
    val model = models().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath)
      .parent(vanillaModel("block/cube_all"))
      .texture("all", new ResourceLocation(Factorium.ModId, matTex(block)))
    genStates(block, _ => model)
    makeBlockItem(block, model)
  }

  def makeConcreteBlock(block: Block): Unit = {
    val model = models().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath)
      .parent(vanillaModel("block/cube_all"))
      .texture("all", new ResourceLocation(Factorium.ModId, concreteTex(block)))
    genStates(block, _ => model)
    makeBlockItem(block, model)
  }

  def makeTopBottomBlock(block: Block): Unit = {
    val name = ForgeRegistries.BLOCKS.getKey(block).getPath
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
      case x if ForgeRegistries.BLOCKS.getKey(x).getPath.startsWith("mat_") => makeMaterialBlock(x)
      case x if ForgeRegistries.BLOCKS.getKey(x).getPath.startsWith("concrete_") => makeConcreteBlock(x)
      case x => makeBlock(x)
    })
  }
}
