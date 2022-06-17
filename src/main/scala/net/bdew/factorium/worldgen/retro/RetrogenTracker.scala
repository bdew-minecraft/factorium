package net.bdew.factorium.worldgen.retro

import net.bdew.factorium.worldgen.WorldGeneration
import net.bdew.factorium.{Config, Factorium}
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.nbt.NBT
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.chunk.ChunkStatus
import net.minecraft.world.level.{ChunkPos, Level}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.Phase
import net.minecraftforge.event.server.ServerStoppedEvent
import net.minecraftforge.event.world.ChunkDataEvent
import net.minecraftforge.fml.LogicalSide
import org.apache.logging.log4j.LogManager

import scala.collection.mutable

case class ChunkRef(pos: ChunkPos, features: Set[String])

object RetrogenTracker {
  private val log = LogManager.getLogger
  private val chunkQueue = mutable.Map.empty[ResourceKey[Level], mutable.Map[ChunkPos, Set[String]]]
  private var totalCounter = 0

  def add(world: Level, pos: ChunkPos, features: Set[String]): Unit = {
    chunkQueue.synchronized {
      val dim = world.dimension()
      if (!chunkQueue.isDefinedAt(dim))
        chunkQueue += dim -> mutable.Map.empty
      chunkQueue(dim) += pos -> features
    }
  }

  def get(world: Level, pos: ChunkPos): Option[Set[String]] = {
    chunkQueue.synchronized {
      chunkQueue.get(world.dimension()).flatMap(_.get(pos))
    }
  }

  def next(world: Level): Option[(ChunkPos, Set[String])] = {
    chunkQueue.synchronized {
      chunkQueue.get(world.dimension()).flatMap(q => q.headOption.map(h => {
        q.remove(h._1)
        h
      }))
    }
  }

  def remaining: Int = chunkQueue.synchronized {
    chunkQueue.map(_._2.size).sum
  }

  def onChunkDataLoad(ev: ChunkDataEvent.Load): Unit = {
    if (!Config.WorldGen.retrogenEnabled()) return
    (ev.getChunk, ev.getWorld) match {
      case (chunk, world: ServerLevel) if chunk.getStatus == ChunkStatus.FULL =>
        val data = ev.getData

        val existing = if (data.contains(Factorium.ModId, Tag.TAG_COMPOUND))
          data.getCompound(Factorium.ModId).getListVals[String]("worldgen").toSet
        else
          Set.empty[String]

        if (WorldGeneration.features.filter(_.isEnabled()).exists(f => !existing.contains(f.id)))
          add(world, chunk.getPos, existing)

      case _ => // skip
    }
  }

  def onChunkDataSave(ev: ChunkDataEvent.Save): Unit = {
    if (!Config.WorldGen.retrogenEnabled()) return
    ev.getWorld match {
      case world: ServerLevel =>
        // if chunk was in queue, save it with previous features, otherwise assume it got everything
        val features = get(world, ev.getChunk.getPos).getOrElse(WorldGeneration.features.filter(_.isEnabled()).map(_.id).toSet)
        val myTag = NBT("worldgen" -> features.toList)
        ev.getData.put(Factorium.ModId, myTag)
      case _ => // skip
    }
  }

  def onWorldTick(ev: TickEvent.WorldTickEvent): Unit = {
    if (!Config.WorldGen.retrogenEnabled() || ev.side != LogicalSide.SERVER || ev.phase != Phase.END) return
    // fixme
//    (next(ev.world), ev.world) match {
//      case (Some((pos, features)), world: ServerLevel) if ev.world.hasChunk(pos.x, pos.z) =>
//        val chunk = world.getChunk(pos.x, pos.z, ChunkStatus.FULL, false)
//        val biomes = mutable.ListBuffer.empty[Holder[Biome]]
//        chunk.getSections.foreach(_.getBiomes.getAll(x=>biomes.addOne(x)))
//        val rng = new Random(world.getSeed)
//        val xSeed = rng.nextLong >> 3
//        val zSeed = rng.nextLong >> 3
//        val genLevel = new RetrogenLevel(world)
//        rng.setSeed(xSeed * pos.x + zSeed * pos.z ^ world.getSeed)
//        for (entry <- WorldGeneration.features if entry.isEnabled() && !features.contains(entry.id) && biomes.exists(x => entry.filter.matches(Biome.getBiomeCategory(x)))) {
//          log.info(s"Generating ${entry.id} in chunk $pos (${world.dimension().location()})")
//          entry.feature.placeWithBiomeCheck(genLevel, world.getChunkSource.getGenerator, rng, new BlockPos(pos.x << 4, 0, pos.z << 4))
//        }
//        chunk.setUnsaved(true)
//        totalCounter += 1
//        log.info(s"Applied retrogen to $totalCounter chunks so far, $remaining chunks left")
//      case _ => // nothing
//    }
  }

  def onServerStopped(ev: ServerStoppedEvent): Unit = {
    chunkQueue.clear()
    totalCounter = 0
  }

  def init(): Unit = {
    MinecraftForge.EVENT_BUS.addListener(onChunkDataSave)
    MinecraftForge.EVENT_BUS.addListener(onChunkDataLoad)
    MinecraftForge.EVENT_BUS.addListener(onWorldTick)
    MinecraftForge.EVENT_BUS.addListener(onServerStopped)
  }
}
