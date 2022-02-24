package net.bdew.advtech.worldgen.features

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.bdew.advtech.Config
import net.bdew.advtech.worldgen.ores.NormalOreGenConfigSection
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState
import net.minecraft.world.level.levelgen.feature.configurations.{FeatureConfiguration, OreConfiguration}
import net.minecraft.world.level.levelgen.feature.{ConfiguredFeature, Feature, FeaturePlaceContext}

import java.util

class OreFeatureNormal extends Feature(OreFeatureConfig.CODEC) {
  override def place(ctx: FeaturePlaceContext[OreFeatureConfig]): Boolean = {
    val cfg = ctx.config()
    if (cfg.section.enabled()) {
      val vanillaConfig = new OreConfiguration(cfg.targets, cfg.section.size(), cfg.section.airExposure())
      Feature.ORE.place(new FeaturePlaceContext(
        ctx.topFeature(), ctx.level(), ctx.chunkGenerator(), ctx.random(), ctx.origin(), vanillaConfig
      ))
    } else false
  }

  def configured(targets: util.List[TargetBlockState], sectionId: String): ConfiguredFeature[OreFeatureConfig, OreFeatureNormal] =
    new ConfiguredFeature(this, OreFeatureConfig(targets, sectionId))
}

case class OreFeatureConfig(targets: util.List[TargetBlockState], sectionId: String) extends FeatureConfiguration {
  def section: NormalOreGenConfigSection = Config.WorldGen.byId(sectionId).cfg.asInstanceOf[NormalOreGenConfigSection]
}

object OreFeatureConfig {
  val CODEC: Codec[OreFeatureConfig] = RecordCodecBuilder.create(app => app.group(
    Codec.list(TargetBlockState.CODEC).fieldOf("targets").forGetter(_.targets),
    Codec.STRING.fieldOf("sectionId").forGetter(_.sectionId)
  ).apply(app, OreFeatureConfig(_, _)))
}