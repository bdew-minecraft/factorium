package net.bdew.advtech.network

import net.bdew.lib.misc.RSMode
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.inventory.AbstractContainerMenu

case class MsgSetRsMode(rsMode: RSMode.Value) extends NetworkHandler.Message

object CodecSetRsMode extends NetworkHandler.Codec[MsgSetRsMode] {
  override def encodeMsg(m: MsgSetRsMode, p: FriendlyByteBuf): Unit =
    p.writeByte(m.rsMode.id)

  override def decodeMsg(p: FriendlyByteBuf): MsgSetRsMode =
    MsgSetRsMode(RSMode(p.readByte()))
}

trait RsModeConfigurableContainer extends AbstractContainerMenu {
  def setRsMode(mode: RSMode.Value): Unit
}