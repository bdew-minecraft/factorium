package net.bdew.factorium.datagen

import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import net.bdew.factorium.Factorium
import net.bdew.factorium.metals.Metals
import net.bdew.factorium.worldgen.ores.OreGenMeteorite
import net.bdew.factorium.worldgen.{WorldgenTemplate, WorldgenType}
import net.minecraft.core.{Holder, HolderSet, Registry, RegistryAccess}
import net.minecraft.data.DataGenerator
import net.minecraft.resources.{RegistryOps, ResourceKey, ResourceLocation}
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraftforge.common.data.{ExistingFileHelper, JsonCodecProvider}
import net.minecraftforge.common.world.BiomeModifier
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier
import net.minecraftforge.registries.ForgeRegistries.Keys

import scala.jdk.CollectionConverters._

class WorldGenProviders(gen: DataGenerator, efh: ExistingFileHelper) {
  val ops: RegistryOps[JsonElement] = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy)
  val modId: String = Factorium.ModId

  private var configuredFeatures = Map.empty[ResourceLocation, ConfiguredFeature[_, _]]
  private var placedFeatures = Map.empty[ResourceLocation, PlacedFeature]
  private var types = Map.empty[WorldgenType, List[Holder[PlacedFeature]]]

  def refHolder[T](reg: ResourceKey[Registry[T]], loc: ResourceLocation): Holder[T] =
    Holder.Reference.createStandAlone(ops.registry(reg).get(), ResourceKey.create(reg, loc))

  def addTemplate(ore: WorldgenTemplate): Unit = {
    val loc = new ResourceLocation(modId, ore.id)
    configuredFeatures += loc -> ore.makeConfiguredFeature
    placedFeatures += loc -> ore.makePlacedFeature(refHolder(Registry.CONFIGURED_FEATURE_REGISTRY, loc))
    types += ore.worldgenType -> (types.getOrElse(ore.worldgenType, List.empty) :+ refHolder(Registry.PLACED_FEATURE_REGISTRY, loc))
  }

  Metals.all.flatMap(x => x.oreGen).foreach(addTemplate)
  addTemplate(OreGenMeteorite)

  private val overrides: Map[ResourceLocation, BiomeModifier] = for ((wgType, features) <- types) yield
    new ResourceLocation(modId, wgType.id) -> new AddFeaturesBiomeModifier(
      new HolderSet.Named(ops.registry(Registry.BIOME_REGISTRY).get, wgType.biomes),
      HolderSet.direct(features.asJava),
      wgType.step
    )

  val biomeModifiersProvider: JsonCodecProvider[BiomeModifier] =
    JsonCodecProvider.forDatapackRegistry(gen, efh, modId, ops, Keys.BIOME_MODIFIERS, overrides.asJava)

  val configuredFeatureProvider: JsonCodecProvider[ConfiguredFeature[_, _]] =
    JsonCodecProvider.forDatapackRegistry(gen, efh, modId, ops, Registry.CONFIGURED_FEATURE_REGISTRY, configuredFeatures.asJava)

  val placedFeatureProvider: JsonCodecProvider[PlacedFeature] =
    JsonCodecProvider.forDatapackRegistry(gen, efh, modId, ops, Registry.PLACED_FEATURE_REGISTRY, placedFeatures.asJava)

}
