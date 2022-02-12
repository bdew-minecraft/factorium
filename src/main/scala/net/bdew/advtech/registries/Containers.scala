package net.bdew.advtech.registries

import net.bdew.advtech.machines.processing.crusher.CrusherContainer
import net.bdew.advtech.machines.processing.smelter.SmelterContainer
import net.bdew.advtech.machines.processing.{ProcessingMachineContainer, ProcessingMachineScreen}
import net.bdew.advtech.upgrades.UpgradeableMachine
import net.bdew.advtech.upgrades.gui.{UpgradesContainer, UpgradesScreen}
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

  val smelter: RegistryObject[MenuType[ProcessingMachineContainer]] =
    registerPositional("smelter", Blocks.smelter.teType) {
      (id, inv, te) => new SmelterContainer(te, inv, id)
    }

  val upgrades: RegistryObject[MenuType[UpgradesContainer]] =
    registerSimple("upgrades") { (id, inv, data) =>
      inv.player.level.getBlockEntity(data.readBlockPos()) match {
        case te: UpgradeableMachine => new UpgradesContainer(te, inv, id)
        case _ => null
      }
    }

  @OnlyIn(Dist.CLIENT)
  override def onClientSetup(ev: FMLClientSetupEvent): Unit = {
    registerScreen(crusher) { (c, i, _) => new ProcessingMachineScreen(c, i) }
    registerScreen(smelter) { (c, i, _) => new ProcessingMachineScreen(c, i) }
    registerScreen(upgrades) { (c, i, _) => new UpgradesScreen(c, i) }
  }
}
