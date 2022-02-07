package net.bdew.advtech.registries

import net.bdew.advtech.machines.crusher.CrusherEntity
import net.bdew.advtech.machines.{BaseMachineBlock, BaseMachineItem}
import net.bdew.lib.managers.BlockManager
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.{OreBlock, SoundType}
import net.minecraft.world.level.material.Material
import net.minecraftforge.registries.RegistryObject

object Blocks extends BlockManager(Items) {
  def oreProps: Properties = props(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(3, 3)

  def machineProps: Properties = props(Material.STONE)
    .sound(SoundType.STONE)
    .strength(2, 8)

  val crusher: Def[BaseMachineBlock[CrusherEntity], CrusherEntity, BaseMachineItem] =
    define("crusher", () => new BaseMachineBlock[CrusherEntity])
      .withTE(new CrusherEntity(_, _, _))
      .withItem(b => new BaseMachineItem(b))
      .register

  val ores: Map[MetalEntry, RegistryObject[OreBlock]] =
    Metals.all.filter(_.registerOre)
      .map(metal => metal -> register(s"ore_${metal.name}", () => new OreBlock(oreProps)))
      .toMap
}
