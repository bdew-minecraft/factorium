package net.bdew.advtech.network

import net.minecraft.network.FriendlyByteBuf

case class MsgCloseUpgrades() extends NetworkHandler.Message

object CodecCloseUpgrades extends NetworkHandler.Codec[MsgCloseUpgrades] {
  override def encodeMsg(m: MsgCloseUpgrades, p: FriendlyByteBuf): Unit = {}
  override def decodeMsg(p: FriendlyByteBuf): MsgCloseUpgrades = MsgCloseUpgrades()
}
