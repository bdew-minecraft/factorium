package net.bdew.factorium.worldgen.features

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.bdew.factorium.Config
import net.bdew.factorium.worldgen.PlacementModifiers
import net.bdew.factorium.worldgen.ores.NormalOreGenConfigSection
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.levelgen.heightproviders.{HeightProvider, UniformHeight}
import net.minecraft.world.level.levelgen.placement.{PlacementContext, PlacementModifier, PlacementModifierType}
import net.minecraft.world.level.levelgen.{VerticalAnchor, WorldGenerationContext}

import java.util.stream
import java.util.stream.Stream

case class HeightRangePlacementConfig(sectionId: String) extends PlacementModifier {
  def section: NormalOreGenConfigSection = Config.WorldGen.byId(sectionId).cfg.asInstanceOf[NormalOreGenConfigSection]

  val heightProvider: HeightProvider = UniformHeight.of(VerticalAnchorConfig(section.minY), VerticalAnchorConfig(section.maxY))

  override def getPositions(ctx: PlacementContext, random: RandomSource, pos: BlockPos): stream.Stream[BlockPos] =
    Stream.of(pos.atY(heightProvider.sample(random, ctx)))

  override def `type`(): PlacementModifierType[_] = PlacementModifiers.heightRangeConfig.get
}

case class VerticalAnchorConfig(getter: () => Int) extends VerticalAnchor {
  override def resolveY(ctx: WorldGenerationContext): Int = getter()
}

class HeightRangePlacementConfigType extends PlacementModifierType[HeightRangePlacementConfig] {
  val CODEC: Codec[HeightRangePlacementConfig] = RecordCodecBuilder.create(app => app.group(
    Codec.STRING.fieldOf("sectionId").forGetter(_.sectionId)
  ).apply(app, HeightRangePlacementConfig(_)))

  override def codec(): Codec[HeightRangePlacementConfig] = CODEC
}