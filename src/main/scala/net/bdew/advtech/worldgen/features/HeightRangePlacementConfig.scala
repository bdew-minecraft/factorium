package net.bdew.advtech.worldgen.features

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.bdew.advtech.Config
import net.bdew.advtech.worldgen.ores.NormalOreGenConfigSection
import net.minecraft.core.BlockPos
import net.minecraft.world.level.levelgen.heightproviders.{HeightProvider, UniformHeight}
import net.minecraft.world.level.levelgen.placement.{PlacementContext, PlacementModifier, PlacementModifierType}
import net.minecraft.world.level.levelgen.{VerticalAnchor, WorldGenerationContext}

import java.util.stream.Stream
import java.util.{Random, stream}

case class HeightRangePlacementConfig(sectionId: String) extends PlacementModifier {
  def section: NormalOreGenConfigSection = Config.WorldGen.byId(sectionId).cfg.asInstanceOf[NormalOreGenConfigSection]

  val heightProvider: HeightProvider = UniformHeight.of(VerticalAnchorConfig(section.minY), VerticalAnchorConfig(section.maxY))

  override def getPositions(ctx: PlacementContext, random: Random, pos: BlockPos): stream.Stream[BlockPos] =
    Stream.of(pos.atY(heightProvider.sample(random, ctx)))

  override def `type`(): PlacementModifierType[_] = HeightRangePlacementConfigType
}

case class VerticalAnchorConfig(getter: () => Int) extends VerticalAnchor(0) {
  override def resolveY(ctx: WorldGenerationContext): Int = getter()
  override def value(): Int = getter()
}

object HeightRangePlacementConfigType extends PlacementModifierType[HeightRangePlacementConfig] {
  val CODEC: Codec[HeightRangePlacementConfig] = RecordCodecBuilder.create(app => app.group(
    Codec.STRING.fieldOf("sectionId").forGetter(_.sectionId)
  ).apply(app, HeightRangePlacementConfig(_)))

  override def codec(): Codec[HeightRangePlacementConfig] = CODEC
}