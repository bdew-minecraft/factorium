package net.bdew.factorium.registries

import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour.{Properties, StateArgumentPredicate, StatePredicate}
import net.minecraft.world.level.material.MapColor

object BlockProps {
  val neverE: StateArgumentPredicate[EntityType[_]] = (_, _, _, _) => false
  val alwaysE: StateArgumentPredicate[EntityType[_]] = (_, _, _, _) => true
  val never: StatePredicate = (_, _, _) => false
  val always: StatePredicate = (_, _, _) => true

  def machineProps: Properties = Blocks.props.mapColor(MapColor.STONE)
    .sound(SoundType.STONE)
    .strength(2, 8)

  def storageProps: Properties = Blocks.props.mapColor(MapColor.METAL)
    .requiresCorrectToolForDrops()
    .strength(5, 6)
    .sound(SoundType.METAL)

  def normalOreProps: Properties = Blocks.props.mapColor(MapColor.STONE)
    .requiresCorrectToolForDrops()
    .strength(3, 3)

  def deepOreProps: Properties = Blocks.props.mapColor(MapColor.DEEPSLATE)
    .requiresCorrectToolForDrops()
    .strength(4.5f, 3)
    .sound(SoundType.DEEPSLATE)

  def netherOreProps: Properties = Blocks.props.mapColor(MapColor.NETHER)
    .requiresCorrectToolForDrops()
    .strength(3, 3)
    .sound(SoundType.NETHER_GOLD_ORE)

  def endOreProps: Properties = Blocks.props.mapColor(MapColor.SAND)
    .requiresCorrectToolForDrops()
    .strength(3, 9)

  def baseGlassprops: Properties = Blocks.props
    .sound(SoundType.GLASS)
    .noOcclusion
    .isValidSpawn(neverE)
    .isRedstoneConductor(never)
    .isSuffocating(never)
    .isViewBlocking(never)

  def glassCrystalProps: Properties =
    baseGlassprops.strength(0.3F)

  def reinforcedGlassProps: Properties =
    baseGlassprops
      .mapColor(DyeColor.BLACK)
      .strength(10.0F, 1200.0F)
      .requiresCorrectToolForDrops()

  def reinforcedConcreteProps(color: DyeColor): Properties =
    Blocks.props
      .mapColor(color)
      .strength(10.0F, 1200.0F)
      .requiresCorrectToolForDrops()

  def glowingConcreteProps(color: DyeColor): Properties =
    Blocks.props
      .mapColor(color)
      .strength(1.8F)
      .requiresCorrectToolForDrops()
      .lightLevel(_ => 15)

  def concretePowderProps(color: DyeColor): Properties =
    Blocks.props
      .mapColor(color)
      .strength(0.5F)
      .sound(SoundType.SAND)

  def glowingConcretePowderProps(color: DyeColor): Properties =
    concretePowderProps(color).lightLevel(_ => 15)

  def glassGlowProps: Properties =
    baseGlassprops
      .mapColor(DyeColor.YELLOW)
      .strength(0.3F)
      .lightLevel(_ => 15)

  def glassDarkProps: Properties =
    baseGlassprops
      .mapColor(DyeColor.BLACK)
      .strength(0.3F)
}
