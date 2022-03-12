package net.bdew.factorium.worldgen.retro

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.{BlockPos, Direction, Holder, RegistryAccess}
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.{SoundEvent, SoundSource}
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.biome.{Biome, BiomeManager}
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.border.WorldBorder
import net.minecraft.world.level.chunk.{ChunkAccess, ChunkSource, ChunkStatus}
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.lighting.LevelLightEngine
import net.minecraft.world.level.material.{Fluid, FluidState}
import net.minecraft.world.level.storage.LevelData
import net.minecraft.world.phys.AABB
import net.minecraft.world.ticks.LevelTickAccess

import java.util
import java.util.Random
import java.util.function.Predicate

class RetrogenLevel(base: ServerLevel) extends WorldGenLevel {

  override def getSeed: Long = base.getSeed
  override def getLevel: ServerLevel = base
  override def nextSubTickCount(): Long = base.nextSubTickCount()
  override def getBlockTicks: LevelTickAccess[Block] = base.getBlockTicks
  override def getFluidTicks: LevelTickAccess[Fluid] = base.getFluidTicks
  override def getLevelData: LevelData = base.getLevelData
  override def getCurrentDifficultyAt(pos: BlockPos): DifficultyInstance = base.getCurrentDifficultyAt(pos)
  override def getServer: MinecraftServer = base.getServer
  override def getChunkSource: ChunkSource = base.getChunkSource
  override def getRandom: Random = base.getRandom
  override def playSound(player: Player, pos: BlockPos, event: SoundEvent, source: SoundSource, volume: Float, pitch: Float): Unit = base.playSound(player, pos, event, source, volume, pitch)
  override def addParticle(options: ParticleOptions, d1: Double, d2: Double, d3: Double, d4: Double, d5: Double, d6: Double): Unit = base.addParticle(options, d1, d2, d3, d4, d5, d6)
  override def levelEvent(player: Player, i1: Int, pos: BlockPos, i2: Int): Unit = base.levelEvent(player, i1, pos, i2)
  override def gameEvent(ent: Entity, event: GameEvent, pos: BlockPos): Unit = base.gameEvent(ent, event, pos)
  override def registryAccess(): RegistryAccess = base.registryAccess()
  override def getChunk(x: Int, z: Int, status: ChunkStatus, flag: Boolean): ChunkAccess = base.getChunk(x, z, status, flag)
  override def getHeight(kind: Heightmap.Types, x: Int, z: Int): Int = base.getHeight(kind, x, z)
  override def getSkyDarken: Int = base.getSkyDarken
  override def getBiomeManager: BiomeManager = base.getBiomeManager
  override def getUncachedNoiseBiome(x: Int, y: Int, z: Int): Holder[Biome] = base.getUncachedNoiseBiome(x, y, z)
  override def isClientSide: Boolean = base.isClientSide
  override def getSeaLevel: Int = base.getSeaLevel
  override def dimensionType(): DimensionType = base.dimensionType()
  override def getWorldBorder: WorldBorder = base.getWorldBorder
  override def getShade(dir: Direction, flag: Boolean): Float = base.getShade(dir, flag)
  override def getLightEngine: LevelLightEngine = base.getLightEngine
  override def isStateAtPosition(pos: BlockPos, predicate: Predicate[BlockState]): Boolean = base.isStateAtPosition(pos, predicate)
  override def isFluidAtPosition(pos: BlockPos, predicate: Predicate[FluidState]): Boolean = base.isFluidAtPosition(pos, predicate)
  override def getBlockEntity(pos: BlockPos): BlockEntity = base.getBlockEntity(pos)
  override def getBlockState(pos: BlockPos): BlockState = base.getBlockState(pos)
  override def getFluidState(pos: BlockPos): FluidState = base.getFluidState(pos)
  override def getEntities(entity: Entity, box: AABB, predicate: Predicate[_ >: Entity]): util.List[Entity] = base.getEntities(entity, box, predicate)
  override def getEntities[T <: Entity](test: EntityTypeTest[Entity, T], box: AABB, predicate: Predicate[_ >: T]): util.List[T] = base.getEntities(test, box, predicate)
  override def players(): util.List[_ <: Player] = base.players()
  override def setBlock(pos: BlockPos, state: BlockState, i1: Int, i2: Int): Boolean = base.setBlock(pos, state, i1, i2)
  override def removeBlock(pos: BlockPos, flag: Boolean): Boolean = base.removeBlock(pos, flag)
  override def destroyBlock(pos: BlockPos, flag: Boolean, entity: Entity, i1: Int): Boolean = base.destroyBlock(pos, flag, entity, i1)

  // This prevents retrogen from waiting for new chunks to be generated, which can cause deadlocks

  override def getChunk(x: Int, z: Int): ChunkAccess = getChunk(x, z, ChunkStatus.EMPTY)
}
