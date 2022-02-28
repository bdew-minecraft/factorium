package net.bdew.factorium.worldgen

import net.bdew.factorium.metals.Metals
import net.bdew.factorium.worldgen.ores.OreGenMeteorite
import net.bdew.lib.config.ConfigSection
import net.minecraftforge.common.ForgeConfigSpec

class WorldGenConfig(spec: ForgeConfigSpec.Builder) extends ConfigSection {
  val retrogenEnabled: () => Boolean = boolVal(spec, "retrogenEnabled", "Disable to prevent generating stuff in existing chunks", true)

  val configs: List[WorldgenConfigured[_ <: ConfigSection]] =
    Metals.all.flatMap(metal =>
      metal.oreGen.map({ case gen: WorldgenTemplate[x] => WorldgenConfigured(gen, gen.makeConfig(spec)) })
    ) ::: List(
      WorldgenConfigured(OreGenMeteorite, OreGenMeteorite.makeConfig(spec))
    )

  val byId: Map[String, WorldgenConfigured[_ <: ConfigSection]] = configs.map(x => x.template.id -> x).toMap
}