package net.bdew.factorium.worldgen.features

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.bdew.factorium.Config
import net.bdew.factorium.worldgen.PlacementModifiers
import net.bdew.factorium.worldgen.ores.OreGenConfigSection
import net.minecraft.core.BlockPos
import net.minecraft.world.level.levelgen.placement.{PlacementContext, PlacementModifier, PlacementModifierType}

import java.util.stream.IntStream
import java.util.{Random, stream}

case class CountPlacementConfig(sectionId: String) extends PlacementModifier {
  def section: OreGenConfigSection = Config.WorldGen.byId(sectionId).cfg.asInstanceOf[OreGenConfigSection]
  override def getPositions(ctx: PlacementContext, random: Random, pos: BlockPos): stream.Stream[BlockPos] =
    IntStream.range(0, section.count()).mapToObj(_ => pos)
  override def `type`(): PlacementModifierType[_] = PlacementModifiers.countConfig.get
}

class CountPlacementConfigType extends PlacementModifierType[CountPlacementConfig] {
  val CODEC: Codec[CountPlacementConfig] = RecordCodecBuilder.create(app => app.group(
    Codec.STRING.fieldOf("sectionId").forGetter(_.sectionId)
  ).apply(app, CountPlacementConfig(_)))

  override def codec(): Codec[CountPlacementConfig] = CODEC
}