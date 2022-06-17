package net.bdew.factorium.machines.pump

import net.bdew.lib.data.base.{DataSlot, DataSlotContainer, UpdateKind}
import net.bdew.lib.nbt.NBT
import net.minecraft.nbt.{CompoundTag, Tag}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.{Fluid, Fluids}
import net.minecraftforge.registries.ForgeRegistries
import org.apache.logging.log4j.{LogManager, Logger}

sealed trait PumpState {

}

object PumpState {
  case class Lowering(toDepth: Int) extends PumpState
  case class Pumping(atDepth: Int, infinite: Boolean, fluid: Fluid) extends PumpState
  case class Scanning(atDepth: Int, fluid: Fluid) extends PumpState
  case object Invalid extends PumpState
}

class DataSlotPumpState(val name: String, val parent: DataSlotContainer) extends DataSlot {
  private var current: PumpState = PumpState.Lowering(1)
  private val logger: Logger = LogManager.getLogger

  setUpdate(UpdateKind.SAVE, UpdateKind.WORLD, UpdateKind.RENDER, UpdateKind.MODEL_DATA)

  def get: PumpState = current
  def change(st: PumpState): Unit = execWithChangeNotify {
    current = st
  }

  override def save(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    t.put(name, current match {
      case PumpState.Invalid => NBT("st" -> "invalid")
      case PumpState.Lowering(d) =>
        NBT(
          "st" -> "lowering",
          "d" -> d,
        )
      case PumpState.Scanning(d, f) =>
        NBT(
          "st" -> "scanning",
          "d" -> d,
          "f" -> ForgeRegistries.FLUIDS.getKey(f).toString,
        )
      case PumpState.Pumping(d, i, f) =>
        NBT(
          "st" -> "pumping",
          "d" -> d,
          "i" -> (if (i) 1.toByte else 0.toByte),
          "f" -> ForgeRegistries.FLUIDS.getKey(f).toString,
        )
    })
  }

  override def load(t: CompoundTag, kind: UpdateKind.Value): Unit = {
    if (t.contains(name, Tag.TAG_COMPOUND)) {
      val myTag = t.getCompound(name)
      current = myTag.getString("st") match {
        case "invalid" =>
          PumpState.Invalid
        case "lowering" =>
          PumpState.Lowering(
            toDepth = myTag.getInt("d")
          )
        case "scanning" =>
          val fid = myTag.getString("f")
          val fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fid))
          if (fluid == null || fluid.isSame(Fluids.EMPTY)) {
            logger.warn(s"Invalid fluid: $fid")
            PumpState.Invalid
          } else {
            PumpState.Scanning(
              atDepth = myTag.getInt("d"),
              fluid
            )
          }
        case "pumping" =>
          val fid = myTag.getString("f")
          val fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fid))
          if (fluid == null || fluid.isSame(Fluids.EMPTY)) {
            logger.warn(s"Invalid fluid: $fid")
            PumpState.Invalid
          } else {
            PumpState.Pumping(
              atDepth = myTag.getInt("d"),
              infinite = myTag.getBoolean("i"),
              fluid
            )
          }
        case x =>
          logger.warn(s"Invalid pump state: $x")
          PumpState.Invalid
      }
    }
  }
}