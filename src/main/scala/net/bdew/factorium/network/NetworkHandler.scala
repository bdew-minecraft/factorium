package net.bdew.factorium.network

import net.bdew.factorium.Factorium
import net.bdew.factorium.machines.sided.SidedItemIOContainer
import net.bdew.lib.network.NetChannel

object NetworkHandler extends NetChannel(Factorium.ModId, "main", "1") {
  regServerContainerHandler(1, CodecSetRsMode, classOf[RsModeConfigurableContainer]) { (msg, cont, _) =>
    cont.setRsMode(msg.rsMode)
  }

  regServerContainerHandler(2, CodecSetItemSidedIO, classOf[SidedItemIOContainer]) { (msg, cont, _) =>
    cont.te.itemIOConfig.set(msg.side, msg.mode)
  }
}





