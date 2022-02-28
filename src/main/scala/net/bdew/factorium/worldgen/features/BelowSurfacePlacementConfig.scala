package net.bdew.factorium.worldgen.features

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.bdew.factorium.Config
import net.bdew.factorium.worldgen.ores.DepthOreGenConfigSection
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.placement.{PlacementContext, PlacementModifier, PlacementModifierType}

import java.util.stream.Stream
import java.util.{Random, stream}

case class BelowSurfacePlacementConfig(sectionId: String) extends PlacementModifier {
  def section: DepthOreGenConfigSection = Config.WorldGen.byId(sectionId).cfg.asInstanceOf[DepthOreGenConfigSection]

  def surfaceHeightAt(ctx: PlacementContext, pos: BlockPos): Int = {
    ctx.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX, pos.getZ)
  }

  override def getPositions(ctx: PlacementContext, random: Random, pos: BlockPos): stream.Stream[BlockPos] = {
    val surface = surfaceHeightAt(ctx, pos)
    Stream.of(pos.atY(Mth.randomBetweenInclusive(random, surface - section.maxDepth(), surface - section.minDepth())))
  }

  override def `type`(): PlacementModifierType[_] = BelowSurfacePlacementConfigType
}

object BelowSurfacePlacementConfigType extends PlacementModifierType[BelowSurfacePlacementConfig] {
  val CODEC: Codec[BelowSurfacePlacementConfig] = RecordCodecBuilder.create(app => app.group(
    Codec.STRING.fieldOf("sectionId").forGetter(_.sectionId)
  ).apply(app, BelowSurfacePlacementConfig(_)))

  override def codec(): Codec[BelowSurfacePlacementConfig] = CODEC
}