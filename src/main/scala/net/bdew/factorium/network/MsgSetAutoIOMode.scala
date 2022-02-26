package net.bdew.factorium.network

import net.bdew.factorium.machines.sided.BlockSide
import net.bdew.factorium.misc.AutoIOMode
import net.minecraft.network.FriendlyByteBuf

case class MsgSetItemSidedIO(side: BlockSide.Value, mode: AutoIOMode.Value) extends NetworkHandler.Message

object CodecSetItemSidedIO extends NetworkHandler.Codec[MsgSetItemSidedIO] {
  override def encodeMsg(m: MsgSetItemSidedIO, p: FriendlyByteBuf): Unit = {
    p.writeByte(m.side.id)
    p.writeByte(m.mode.id)
  }

  override def decodeMsg(p: FriendlyByteBuf): MsgSetItemSidedIO = {
    val side = BlockSide(p.readByte())
    val mode = AutoIOMode(p.readByte())
    MsgSetItemSidedIO(side, mode)
  }
}
