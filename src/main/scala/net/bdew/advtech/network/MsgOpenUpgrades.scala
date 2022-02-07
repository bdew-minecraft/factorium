package net.bdew.advtech.network

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.AbstractContainerMenu

case class MsgOpenUpgrades() extends NetworkHandler.Message

object CodecMsgOpenUpgrades extends NetworkHandler.Codec[MsgOpenUpgrades] {
  override def encodeMsg(m: MsgOpenUpgrades, p: FriendlyByteBuf): Unit = {}
  override def decodeMsg(p: FriendlyByteBuf): MsgOpenUpgrades = MsgOpenUpgrades()
}

trait UpgradeableContainer extends AbstractContainerMenu {
  def openUpgrades(player: ServerPlayer): Unit
}