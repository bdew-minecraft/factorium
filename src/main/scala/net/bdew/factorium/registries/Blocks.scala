package net.bdew.factorium.registries

import net.bdew.factorium.machines.BaseMachineItem
import net.bdew.factorium.machines.alloy.AlloySmelterEntity
import net.bdew.factorium.machines.processing.crusher.CrusherEntity
import net.bdew.factorium.machines.processing.grinder.GrinderEntity
import net.bdew.factorium.machines.processing.pulverizer.PulverizerEntity
import net.bdew.factorium.machines.processing.smelter.SmelterEntity
import net.bdew.factorium.machines.worker.WorkerMachineBlock
import net.bdew.factorium.metals._
import net.bdew.lib.managers.BlockManager
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.{OreBlock, SoundType}
import net.minecraft.world.level.material.{Material, MaterialColor}

object Blocks extends BlockManager(Items) {
  def normalOreProps: Properties = props(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(3, 3)

  def deepOreProps: Properties = props(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(4.5f, 3)
    .color(MaterialColor.DEEPSLATE)
    .sound(SoundType.DEEPSLATE)

  def netherOreProps: Properties = props(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(3, 3)
    .color(MaterialColor.NETHER)
    .sound(SoundType.NETHER_GOLD_ORE)

  def endOreProps: Properties = props(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(3, 9)
    .color(MaterialColor.SAND)

  def storageProps: Properties = props(Material.METAL)
    .requiresCorrectToolForDrops()
    .strength(5, 6)
    .sound(SoundType.METAL)

  def machineProps: Properties = props(Material.STONE)
    .sound(SoundType.STONE)
    .strength(2, 8)

  val crusher: Def[WorkerMachineBlock[CrusherEntity], CrusherEntity, BaseMachineItem] =
    define("crusher", () => new WorkerMachineBlock[CrusherEntity])
      .withTE(new CrusherEntity(_, _, _))
      .withItem(b => new BaseMachineItem(b))
      .register

  val grinder: Def[WorkerMachineBlock[GrinderEntity], GrinderEntity, BaseMachineItem] =
    define("grinder", () => new WorkerMachineBlock[GrinderEntity])
      .withTE(new GrinderEntity(_, _, _))
      .withItem(b => new BaseMachineItem(b))
      .register

  val pulverizer: Def[WorkerMachineBlock[PulverizerEntity], PulverizerEntity, BaseMachineItem] =
    define("pulverizer", () => new WorkerMachineBlock[PulverizerEntity])
      .withTE(new PulverizerEntity(_, _, _))
      .withItem(b => new BaseMachineItem(b))
      .register

  val smelter: Def[WorkerMachineBlock[SmelterEntity], SmelterEntity, BaseMachineItem] =
    define("smelter", () => new WorkerMachineBlock[SmelterEntity])
      .withTE(new SmelterEntity(_, _, _))
      .withItem(b => new BaseMachineItem(b))
      .register

  val alloySmelter: Def[WorkerMachineBlock[AlloySmelterEntity], AlloySmelterEntity, BaseMachineItem] =
    define("alloy", () => new WorkerMachineBlock[AlloySmelterEntity])
      .withTE(new AlloySmelterEntity(_, _, _))
      .withItem(b => new BaseMachineItem(b))
      .register

  for (metal <- Metals.all; (block, ref) <- metal.blocks if ref.isOwned) {
    block.group match {
      case MetalItemType.groupStorageBlock =>
        define(metal.registryName(block), () => MetalBlock(storageProps, block, metal)).withItem(MetalBlockItem(_, defaultItemProps)).register
      case MetalItemType.groupOreNormal =>
        define(metal.registryName(block), () => MetalOreBlock(normalOreProps, block, metal)).withItem(MetalBlockItem(_, defaultItemProps)).register
      case MetalItemType.groupOreDeep =>
        define(metal.registryName(block), () => MetalOreBlock(deepOreProps, block, metal)).withItem(MetalBlockItem(_, defaultItemProps)).register
      case MetalItemType.groupOreNether =>
        define(metal.registryName(block), () => MetalOreBlock(netherOreProps, block, metal)).withItem(MetalBlockItem(_, defaultItemProps)).register
      case MetalItemType.groupOreEnd =>
        define(metal.registryName(block), () => MetalOreBlock(endOreProps, block, metal)).withItem(MetalBlockItem(_, defaultItemProps)).register
      case _ => // pass
    }
  }

  val meteoriteOre: Blocks.DefBI[OreBlock, BlockItem] =
    define("mat_meteorite_ore",
      () => new OreBlock(props(Material.STONE)
        .requiresCorrectToolForDrops()
        .strength(5, 3))
    ).withDefaultItem.register
}
