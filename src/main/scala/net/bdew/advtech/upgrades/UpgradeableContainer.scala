package net.bdew.advtech.upgrades

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.AbstractContainerMenu

trait UpgradeableContainer extends AbstractContainerMenu {
  def openUpgrades(player: ServerPlayer): Unit
}