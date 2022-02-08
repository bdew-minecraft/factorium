package net.bdew.advtech.network

import net.bdew.advtech.AdvTech
import net.bdew.advtech.upgrades.UpgradeableContainer
import net.bdew.advtech.upgrades.gui.UpgradesContainer
import net.bdew.lib.network.NetChannel

object NetworkHandler extends NetChannel(AdvTech.ModId, "main", "1") {
  regServerContainerHandler(1, CodecSetRsMode, classOf[RsModeConfigurableContainer]) { (msg, cont, _) =>
    cont.setRsMode(msg.rsMode)
  }

  regServerContainerHandler(2, CodecSetAutoIOMode, classOf[AutoIOConfigurableContainer]) { (msg, cont, _) =>
    cont.setAutoIoMode(msg.autoIOMode)
  }

  regServerContainerHandler(3, CodecOpenUpgrades, classOf[UpgradeableContainer]) { (_, cont, ctx) =>
    cont.openUpgrades(ctx.getSender)
  }

  regServerContainerHandler(4, CodecCloseUpgrades, classOf[UpgradesContainer]) { (_, cont, ctx) =>
    cont.reopenMachine(ctx.getSender)
  }
}





