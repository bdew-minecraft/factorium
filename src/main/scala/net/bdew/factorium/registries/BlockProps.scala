package net.bdew.factorium.registries

import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockBehaviour.{Properties, StateArgumentPredicate, StatePredicate}
import net.minecraft.world.level.material.{Material, MaterialColor}

object BlockProps {
  val neverE: StateArgumentPredicate[EntityType[_]] = (_, _, _, _) => false
  val alwaysE: StateArgumentPredicate[EntityType[_]] = (_, _, _, _) => true
  val never: StatePredicate = (_, _, _) => false
  val always: StatePredicate = (_, _, _) => true

  def machineProps: Properties = Blocks.props(Material.STONE)
    .sound(SoundType.STONE)
    .strength(2, 8)

  def storageProps: Properties = Blocks.props(Material.METAL)
    .requiresCorrectToolForDrops()
    .strength(5, 6)
    .sound(SoundType.METAL)

  def normalOreProps: Properties = Blocks.props(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(3, 3)

  def deepOreProps: Properties = Blocks.props(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(4.5f, 3)
    .color(MaterialColor.DEEPSLATE)
    .sound(SoundType.DEEPSLATE)

  def netherOreProps: Properties = Blocks.props(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(3, 3)
    .color(MaterialColor.NETHER)
    .sound(SoundType.NETHER_GOLD_ORE)

  def endOreProps: Properties = Blocks.props(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(3, 9)
    .color(MaterialColor.SAND)

  def baseGlassprops(color: MaterialColor): Properties = BlockBehaviour.Properties.of(Material.GLASS, color)
    .sound(SoundType.GLASS)
    .noOcclusion
    .isValidSpawn(neverE)
    .isRedstoneConductor(never)
    .isSuffocating(never)
    .isViewBlocking(never)

  def glassCrystalProps: Properties =
    baseGlassprops(Material.GLASS.getColor)
      .strength(0.3F)

  def reinforcedGlassProps: Properties =
    BlockProps.baseGlassprops(DyeColor.BLACK.getMaterialColor)
      .strength(10.0F, 1200.0F)
      .requiresCorrectToolForDrops()

  def glassGlowProps: Properties =
    baseGlassprops(DyeColor.YELLOW.getMaterialColor)
      .strength(0.3F).lightLevel(_ => 15)

  def glassDarkProps: Properties =
    baseGlassprops(DyeColor.BLACK.getMaterialColor)
      .strength(0.3F)
}
