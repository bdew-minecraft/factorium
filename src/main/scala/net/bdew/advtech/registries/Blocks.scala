package net.bdew.advtech.registries

import net.bdew.advtech.machines.BaseMachineItem
import net.bdew.advtech.machines.alloy.AlloySmelterEntity
import net.bdew.advtech.machines.processing.crusher.CrusherEntity
import net.bdew.advtech.machines.processing.grinder.GrinderEntity
import net.bdew.advtech.machines.processing.pulverizer.PulverizerEntity
import net.bdew.advtech.machines.processing.smelter.SmelterEntity
import net.bdew.advtech.machines.worker.WorkerMachineBlock
import net.bdew.advtech.metals.{MetalItemType, Metals}
import net.bdew.lib.managers.BlockManager
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
        simple(metal.registryName(block), storageProps)
      case MetalItemType.groupOreNormal =>
        define(metal.registryName(block), () => new OreBlock(normalOreProps)).withDefaultItem.register
      case MetalItemType.groupOreDeep =>
        define(metal.registryName(block), () => new OreBlock(deepOreProps)).withDefaultItem.register
      case _ => // pass
    }
  }

}
