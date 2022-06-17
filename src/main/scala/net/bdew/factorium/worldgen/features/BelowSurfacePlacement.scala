package net.bdew.factorium.worldgen.features

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.bdew.factorium.worldgen.PlacementModifiers
import net.minecraft.core.BlockPos
import net.minecraft.util.{Mth, RandomSource}
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.placement.{PlacementContext, PlacementModifier, PlacementModifierType}

import java.util.stream
import java.util.stream.Stream

case class BelowSurfacePlacement(minDepth: Int, maxDepth: Int) extends PlacementModifier {
  def surfaceHeightAt(ctx: PlacementContext, pos: BlockPos): Int = {
    ctx.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX, pos.getZ)
  }

  override def getPositions(ctx: PlacementContext, random: RandomSource, pos: BlockPos): stream.Stream[BlockPos] = {
    val surface = surfaceHeightAt(ctx, pos)
    Stream.of(pos.atY(Mth.randomBetweenInclusive(random, surface - maxDepth, surface - minDepth)))
  }

  override def `type`(): PlacementModifierType[_] = PlacementModifiers.belowSurface.get
}

class BelowSurfacePlacementType extends PlacementModifierType[BelowSurfacePlacement] {
  val CODEC: Codec[BelowSurfacePlacement] = RecordCodecBuilder.create(app => app.group(
    Codec.INT.fieldOf("minDepth").forGetter(_.minDepth),
    Codec.INT.fieldOf("maxDepth").forGetter(_.maxDepth)
  ).apply(app, BelowSurfacePlacement(_, _)))

  override def codec(): Codec[BelowSurfacePlacement] = CODEC
}