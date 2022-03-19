package net.bdew.factorium.network

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.inventory.AbstractContainerMenu

case class MsgClearBuffers() extends NetworkHandler.Message

object CodecClearBuffers extends NetworkHandler.Codec[MsgClearBuffers] {
  override def encodeMsg(m: MsgClearBuffers, p: FriendlyByteBuf): Unit = {
  }

  override def decodeMsg(p: FriendlyByteBuf): MsgClearBuffers = {
    MsgClearBuffers()
  }
}

trait ClearableContainer extends AbstractContainerMenu {
  def clearBuffers(): Unit
}