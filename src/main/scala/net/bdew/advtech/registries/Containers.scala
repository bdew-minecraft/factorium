package net.bdew.advtech.registries

import net.bdew.advtech.machines.processing.crusher.CrusherContainer
import net.bdew.advtech.machines.processing.grinder.GrinderContainer
import net.bdew.advtech.machines.processing.pulverizer.PulverizerContainer
import net.bdew.advtech.machines.processing.smelter.SmelterContainer
import net.bdew.advtech.machines.processing.{ProcessingMachineContainer, ProcessingMachineScreen}
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

  @OnlyIn(Dist.CLIENT)
  override def onClientSetup(ev: FMLClientSetupEvent): Unit = {
    registerScreen(crusher) { (c, i, _) => new ProcessingMachineScreen(c, i) }
    registerScreen(grinder) { (c, i, _) => new ProcessingMachineScreen(c, i) }
    registerScreen(pulverizer) { (c, i, _) => new ProcessingMachineScreen(c, i) }
    registerScreen(smelter) { (c, i, _) => new ProcessingMachineScreen(c, i) }
  }
}
