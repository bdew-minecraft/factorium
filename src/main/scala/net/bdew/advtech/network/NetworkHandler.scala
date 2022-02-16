package net.bdew.advtech.network

import net.bdew.advtech.AdvTech
import net.bdew.advtech.machines.sided.SidedItemIOContainer
import net.bdew.lib.network.NetChannel

object NetworkHandler extends NetChannel(AdvTech.ModId, "main", "1") {
  regServerContainerHandler(1, CodecSetRsMode, classOf[RsModeConfigurableContainer]) { (msg, cont, _) =>
    cont.setRsMode(msg.rsMode)
  }

  regServerContainerHandler(2, CodecSetItemSidedIO, classOf[SidedItemIOContainer]) { (msg, cont, _) =>
    cont.te.itemIOConfig.set(msg.side, msg.mode)
  }
}





