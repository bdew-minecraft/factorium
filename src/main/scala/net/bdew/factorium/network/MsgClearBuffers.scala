package net.bdew.factorium.network

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.inventory.AbstractContainerMenu

case class MsgClearBuffers(slot: Int) extends NetworkHandler.Message

object CodecClearBuffers extends NetworkHandler.Codec[MsgClearBuffers] {
  override def encodeMsg(m: MsgClearBuffers, p: FriendlyByteBuf): Unit = {
    p.writeInt(m.slot)
  }

  override def decodeMsg(p: FriendlyByteBuf): MsgClearBuffers = {
    MsgClearBuffers(p.readInt())
  }
}

trait ClearableContainer extends AbstractContainerMenu {
  def clearBuffers(slot: Int): Unit
}