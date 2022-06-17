package net.bdew.factorium.worldgen

import net.minecraft.tags.{BiomeTags, TagKey}
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep

case class WorldgenType(id: String, biomes: TagKey[Biome], step: GenerationStep.Decoration)

object WorldgenType {
  val OresOverworld: WorldgenType = WorldgenType("ores_overworld", BiomeTags.IS_OVERWORLD, GenerationStep.Decoration.UNDERGROUND_ORES)
  val OresNether: WorldgenType = WorldgenType("ores_nether", BiomeTags.IS_NETHER, GenerationStep.Decoration.UNDERGROUND_ORES)
  val OresEnd: WorldgenType = WorldgenType("ores_end", BiomeTags.IS_END, GenerationStep.Decoration.UNDERGROUND_ORES)
}