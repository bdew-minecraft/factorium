package net.bdew.advtech.machines.sided

import net.bdew.advtech.misc.AutoIOMode
import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.minecraft.nbt.{CompoundTag, Tag}

case class DataSlotSidedIOConfig(name: String, parent: DataSlotContainer) extends DataSlot {
  private var map: Map[BlockSide.Value, AutoIOMode.Value] = BlockSide.values.map(x => x -> AutoIOMode.NONE).toMap

  setUpdate(UpdateKind.SAVE, UpdateKind.GUI)

  def get(side: BlockSide.Value): AutoIOMode.Value = map(side)

  def set(side: BlockSide.Value, mode: AutoIOMode.Value): Unit =
    execWithChangeNotify {
      map += side -> mode
    }

  override def save(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    val arr = new Array[Byte](6)
    for ((dir, mode) <- map) arr(dir.id) = mode.id.toByte
    t.putByteArray(name, arr)
  }

  override def load(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    if (t.contains(name, Tag.TAG_BYTE_ARRAY)) {
      map = t.getByteArray(name).zipWithIndex
        .map { case (mode, dir) => BlockSide(dir) -> AutoIOMode(mode) }
        .toMap
    }
  }
}
