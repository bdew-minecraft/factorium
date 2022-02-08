package net.bdew.advtech.network

import net.minecraft.network.FriendlyByteBuf

case class MsgOpenUpgrades() extends NetworkHandler.Message

object CodecOpenUpgrades extends NetworkHandler.Codec[MsgOpenUpgrades] {
  override def encodeMsg(m: MsgOpenUpgrades, p: FriendlyByteBuf): Unit = {}
  override def decodeMsg(p: FriendlyByteBuf): MsgOpenUpgrades = MsgOpenUpgrades()
}