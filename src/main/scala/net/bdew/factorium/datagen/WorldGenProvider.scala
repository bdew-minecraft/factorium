package net.bdew.factorium.datagen

import net.bdew.factorium.Factorium
import net.bdew.factorium.metals.Metals
import net.bdew.factorium.worldgen.WorldgenTemplate
import net.bdew.factorium.worldgen.ores.OreGenMeteorite
import net.minecraft.core._
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.{ResourceKey, ResourceLocation}
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier
import net.minecraftforge.registries.ForgeRegistries

import java.util
import java.util.concurrent.CompletableFuture
import scala.jdk.CollectionConverters._

class WorldGenProvider(out: PackOutput, lookup: CompletableFuture[HolderLookup.Provider])
  extends DatapackBuiltinEntriesProvider(out, lookup, WorldGenBuilder.build, util.Set.of(WorldGenBuilder.modId)) {
  override def getName: String = "Worldgen"
}

object WorldGenBuilder {
  val modId: String = Factorium.ModId

  private val templates: List[WorldgenTemplate] = Metals.all.flatMap(x => x.oreGen) :+ OreGenMeteorite

  def key[T](reg: ResourceKey[Registry[T]], id: String): ResourceKey[T] =
    ResourceKey.create(reg, new ResourceLocation(modId, id))

  def build: RegistrySetBuilder = new RegistrySetBuilder()
    .add(Registries.CONFIGURED_FEATURE, (ctx: BootstapContext[ConfiguredFeature[_, _]]) => {
      templates.foreach(tpl => ctx.register(key(Registries.CONFIGURED_FEATURE, tpl.id), tpl.makeConfiguredFeature))
    })
    .add(Registries.PLACED_FEATURE, (ctx: BootstapContext[PlacedFeature]) => {
      val cfGetter = ctx.lookup(Registries.CONFIGURED_FEATURE)
      templates.foreach(tpl => ctx.register(key(Registries.PLACED_FEATURE, tpl.id), tpl.makePlacedFeature(cfGetter.getOrThrow(key(Registries.CONFIGURED_FEATURE, tpl.id)))))
    })
    .add(ForgeRegistries.Keys.BIOME_MODIFIERS, (ctx: BootstapContext[BiomeModifier]) => {
      val pfGetter = ctx.lookup(Registries.PLACED_FEATURE)
      val biomeGetter = ctx.lookup(Registries.BIOME)
      templates.groupBy(_.worldgenType)
        .foreach { case (wgType, templates) =>
          ctx.register(key(ForgeRegistries.Keys.BIOME_MODIFIERS, wgType.id),
            new AddFeaturesBiomeModifier(
              biomeGetter.getOrThrow(wgType.biomes),
              HolderSet.direct(templates.map(x => pfGetter.getOrThrow(key(Registries.PLACED_FEATURE, x.id))).asJava),
              wgType.step
            )
          )
        }
    })
}
