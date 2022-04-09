package net.bdew.factorium.registries

import net.bdew.factorium.blocks._
import net.bdew.factorium.machines.BaseMachineItem
import net.bdew.factorium.machines.alloy.AlloySmelterEntity
import net.bdew.factorium.machines.extruder.ExtruderEntity
import net.bdew.factorium.machines.mixer.{MixerBlock, MixerEntity}
import net.bdew.factorium.machines.processing.crusher.CrusherEntity
import net.bdew.factorium.machines.processing.grinder.GrinderEntity
import net.bdew.factorium.machines.processing.pulverizer.PulverizerEntity
import net.bdew.factorium.machines.processing.smelter.SmelterEntity
import net.bdew.factorium.machines.pump.{PumpBlock, PumpEntity}
import net.bdew.factorium.machines.worker.WorkerMachineBlock
import net.bdew.factorium.metals._
import net.bdew.lib.managers.BlockManager
import net.minecraft.world.item.{BlockItem, DyeColor}
import net.minecraft.world.level.block.OreBlock
import net.minecraft.world.level.material.Material
import net.minecraftforge.registries.RegistryObject

object Blocks extends BlockManager(Items) {
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

  val extruder: Def[WorkerMachineBlock[ExtruderEntity], ExtruderEntity, BaseMachineItem] =
    define("extruder", () => new WorkerMachineBlock[ExtruderEntity])
      .withTE(new ExtruderEntity(_, _, _))
      .withItem(b => new BaseMachineItem(b))
      .register

  val pump: Def[PumpBlock, PumpEntity, BaseMachineItem] =
    define("pump", () => new PumpBlock)
      .withTE(new PumpEntity(_, _, _))
      .withItem(b => new BaseMachineItem(b))
      .register

  val mixer: Def[MixerBlock, MixerEntity, BaseMachineItem] =
    define("mixer", () => new MixerBlock)
      .withTE(new MixerEntity(_, _, _))
      .withItem(b => new BaseMachineItem(b))
      .register

  for (metal <- Metals.all; (block, ref) <- metal.blocks if ref.isOwned) {
    block.group match {
      case MetalItemType.groupStorageBlock =>
        define(metal.registryName(block), () => MetalBlock(BlockProps.storageProps, block, metal)).withItem(MetalBlockItem(_, defaultItemProps)).register
      case MetalItemType.groupOreNormal =>
        define(metal.registryName(block), () => MetalOreBlock(BlockProps.normalOreProps, block, metal)).withItem(MetalBlockItem(_, defaultItemProps)).register
      case MetalItemType.groupOreDeep =>
        define(metal.registryName(block), () => MetalOreBlock(BlockProps.deepOreProps, block, metal)).withItem(MetalBlockItem(_, defaultItemProps)).register
      case MetalItemType.groupOreNether =>
        define(metal.registryName(block), () => MetalOreBlock(BlockProps.netherOreProps, block, metal)).withItem(MetalBlockItem(_, defaultItemProps)).register
      case MetalItemType.groupOreEnd =>
        define(metal.registryName(block), () => MetalOreBlock(BlockProps.endOreProps, block, metal)).withItem(MetalBlockItem(_, defaultItemProps)).register
      case _ => // pass
    }
  }

  val meteoriteOre: Blocks.DefBI[OreBlock, BlockItem] =
    define("mat_meteorite_ore",
      () => new OreBlock(props(Material.STONE)
        .requiresCorrectToolForDrops()
        .strength(5, 3))
    ).withDefaultItem.register

  val crystalGlass: Blocks.DefBI[GlassBlock, BlockItem] = define("glass_crystal", () => new GlassBlock(BlockProps.glassCrystalProps))
    .withDefaultItem
    .register

  val reinforcedGlass: Blocks.DefBI[GlassBlock, BlockItem] = define("glass_reinforced", () => new GlassBlock(BlockProps.reinforcedGlassProps))
    .withDefaultItem
    .register

  val darkGlass: Blocks.DefBI[GlassBlockDark, BlockItem] = define("glass_dark", () => new GlassBlockDark())
    .withDefaultItem
    .register

  val glowGlass: Blocks.DefBI[GlassBlock, BlockItem] = define("glass_glow", () => new GlassBlock(BlockProps.glassGlowProps))
    .withDefaultItem
    .register

  val reinforcedConcrete: Map[DyeColor, RegistryObject[ReinforcedConcreteBlock]] =
    DyeColor.values().map(c => c ->
      define(s"concrete_solid_reinforced_${c.getName}", () => new ReinforcedConcreteBlock(c))
        .withItem(DyedBlockItem).register.block
    ).toMap

  val reinforcedConcretePowder: Map[DyeColor, RegistryObject[ReinforcedConcretePowderBlock]] =
    DyeColor.values().map(c => c ->
      define(s"concrete_powder_reinforced_${c.getName}", () => new ReinforcedConcretePowderBlock(c, reinforcedConcrete(c).get()))
        .withItem(DyedBlockItem).register.block
    ).toMap


  val glowingConcrete: Map[DyeColor, RegistryObject[GlowingConcreteBlock]] =
    DyeColor.values().map(c => c ->
      define(s"concrete_solid_glowing_${c.getName}", () => new GlowingConcreteBlock(c))
        .withItem(DyedBlockItem).register.block
    ).toMap

  val glowingConcretePowder: Map[DyeColor, RegistryObject[GlowingConcretePowderBlock]] =
    DyeColor.values().map(c => c ->
      define(s"concrete_powder_glowing_${c.getName}", () => new GlowingConcretePowderBlock(c, glowingConcrete(c).get()))
        .withItem(DyedBlockItem).register.block
    ).toMap

}
