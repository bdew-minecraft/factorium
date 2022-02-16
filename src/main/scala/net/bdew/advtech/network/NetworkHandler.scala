package net.bdew.advtech.network

import net.bdew.advtech.AdvTech
import net.bdew.lib.network.NetChannel

object NetworkHandler extends NetChannel(AdvTech.ModId, "main", "1") {
  regServerContainerHandler(1, CodecSetRsMode, classOf[RsModeConfigurableContainer]) { (msg, cont, _) =>
    cont.setRsMode(msg.rsMode)
  }

  regServerContainerHandler(2, CodecSetAutoIOMode, classOf[AutoIOConfigurableContainer]) { (msg, cont, _) =>
    cont.setAutoIoMode(msg.autoIOMode)
  }
}





