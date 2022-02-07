package net.bdew.advtech.network

import net.bdew.advtech.misc.AutoIOMode
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.inventory.AbstractContainerMenu

case class MsgSetAutoIOMode(autoIOMode: AutoIOMode.Value) extends NetworkHandler.Message

object CodecSetAutoIOMode extends NetworkHandler.Codec[MsgSetAutoIOMode] {
  override def encodeMsg(m: MsgSetAutoIOMode, p: FriendlyByteBuf): Unit =
    p.writeByte(m.autoIOMode.id)

  override def decodeMsg(p: FriendlyByteBuf): MsgSetAutoIOMode =
    MsgSetAutoIOMode(AutoIOMode(p.readByte()))
}

trait AutoIOConfigurableContainer extends AbstractContainerMenu {
  def setAutoIoMode(mode: AutoIOMode.Value): Unit
}