package net.bdew.advtech.registries

import net.bdew.advtech.machines.crusher.{CrusherContainer, CrusherScreen}
import net.bdew.lib.managers.ContainerManager
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.registries.RegistryObject

object Containers extends ContainerManager {
  val crusher: RegistryObject[MenuType[CrusherContainer]] =
    registerPositional("crusher", Blocks.crusher.teType) {
      (id, inv, te) => new CrusherContainer(te, inv, id)
    }

  @OnlyIn(Dist.CLIENT)
  override def onClientSetup(ev: FMLClientSetupEvent): Unit = {
    registerScreen(crusher) { (c, i, _) => new CrusherScreen(c, i) }
  }
}
