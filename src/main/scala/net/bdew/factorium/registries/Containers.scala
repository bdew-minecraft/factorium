package net.bdew.factorium.registries

import net.bdew.factorium.machines.alloy.{AlloySmelterContainer, AlloySmelterScreen}
import net.bdew.factorium.machines.extruder.{ExtruderContainer, ExtruderScreen}
import net.bdew.factorium.machines.mixer.{MixerContainer, MixerScreen}
import net.bdew.factorium.machines.processing.crusher.CrusherContainer
import net.bdew.factorium.machines.processing.grinder.GrinderContainer
import net.bdew.factorium.machines.processing.pulverizer.PulverizerContainer
import net.bdew.factorium.machines.processing.smelter.SmelterContainer
import net.bdew.factorium.machines.processing.{ProcessingMachineContainer, ProcessingMachineScreen}
import net.bdew.factorium.machines.pump.{PumpContainer, PumpScreen}
import net.bdew.lib.managers.ContainerManager
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.registries.RegistryObject

object Containers extends ContainerManager {
  val crusher: RegistryObject[MenuType[ProcessingMachineContainer]] =
    registerPositional("crusher", Blocks.crusher.teType) {
      (id, inv, te) => new CrusherContainer(te, inv, id)
    }

  val grinder: RegistryObject[MenuType[ProcessingMachineContainer]] =
    registerPositional("grinder", Blocks.grinder.teType) {
      (id, inv, te) => new GrinderContainer(te, inv, id)
    }

  val pulverizer: RegistryObject[MenuType[ProcessingMachineContainer]] =
    registerPositional("pulverizer", Blocks.pulverizer.teType) {
      (id, inv, te) => new PulverizerContainer(te, inv, id)
    }

  val smelter: RegistryObject[MenuType[ProcessingMachineContainer]] =
    registerPositional("smelter", Blocks.smelter.teType) {
      (id, inv, te) => new SmelterContainer(te, inv, id)
    }

  val alloySmelter: RegistryObject[MenuType[AlloySmelterContainer]] =
    registerPositional("alloy", Blocks.alloySmelter.teType) {
      (id, inv, te) => new AlloySmelterContainer(te, inv, id)
    }

  val extruder: RegistryObject[MenuType[ExtruderContainer]] =
    registerPositional("extruder", Blocks.extruder.teType) {
      (id, inv, te) => new ExtruderContainer(te, inv, id)
    }

  val pump: RegistryObject[MenuType[PumpContainer]] =
    registerPositional("pump", Blocks.pump.teType) {
      (id, inv, te) => new PumpContainer(te, inv, id)
    }

  val mixer: RegistryObject[MenuType[MixerContainer]] =
    registerPositional("mixer", Blocks.mixer.teType) {
      (id, inv, te) => new MixerContainer(te, inv, id)
    }

  @OnlyIn(Dist.CLIENT)
  override def onClientSetup(ev: FMLClientSetupEvent): Unit = {
    registerScreen(crusher) { (c, i, _) => new ProcessingMachineScreen(c, i) }
    registerScreen(grinder) { (c, i, _) => new ProcessingMachineScreen(c, i) }
    registerScreen(pulverizer) { (c, i, _) => new ProcessingMachineScreen(c, i) }
    registerScreen(smelter) { (c, i, _) => new ProcessingMachineScreen(c, i) }
    registerScreen(alloySmelter) { (c, i, _) => new AlloySmelterScreen(c, i) }
    registerScreen(extruder) { (c, i, _) => new ExtruderScreen(c, i) }
    registerScreen(pump) { (c, i, _) => new PumpScreen(c, i) }
    registerScreen(mixer) { (c, i, _) => new MixerScreen(c, i) }
  }
}
