package net.bdew.advtech.registries

import net.bdew.advtech.machines.crusher.CrusherEntity
import net.bdew.advtech.machines.{BaseMachineBlock, BaseMachineItem}
import net.bdew.lib.managers.BlockManager
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.{OreBlock, SoundType}
import net.minecraft.world.level.material.Material

object Blocks extends BlockManager(Items) {
  def oreProps: Properties = props(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(3, 3)

  def storageProps = props(Material.METAL)
    .requiresCorrectToolForDrops()
    .strength(5, 6)
    .sound(SoundType.METAL)

  def machineProps: Properties = props(Material.STONE)
    .sound(SoundType.STONE)
    .strength(2, 8)

  val crusher: Def[BaseMachineBlock[CrusherEntity], CrusherEntity, BaseMachineItem] =
    define("crusher", () => new BaseMachineBlock[CrusherEntity])
      .withTE(new CrusherEntity(_, _, _))
      .withItem(b => new BaseMachineItem(b))
      .register

  val ores: Map[MetalEntry, Blocks.DefBI[OreBlock, BlockItem]] =
    Metals.all.filter(_.registerOre)
      .map(metal => metal ->
        define(s"mat_${metal.name}_ore", () => new OreBlock(oreProps))
          .withDefaultItem.register
      ).toMap

  for (metal <- Metals.all) {
    if (metal.registerBlock) simple(s"mat_${metal.name}_block", storageProps)
  }
}
