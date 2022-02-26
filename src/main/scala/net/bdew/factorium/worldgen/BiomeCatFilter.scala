package net.bdew.factorium.worldgen

import net.minecraft.world.level.biome.Biome.BiomeCategory

class BiomeCatFilter(val matches: BiomeCategory => Boolean)

object BiomeCatFilter {
  val normal = new BiomeCatFilter(x => x != BiomeCategory.NETHER && x != BiomeCategory.THEEND)
  val nether = new BiomeCatFilter(_ == BiomeCategory.NETHER)
  val end = new BiomeCatFilter(_ == BiomeCategory.THEEND)
}
