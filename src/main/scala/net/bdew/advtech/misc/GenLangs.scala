package net.bdew.advtech.misc

import net.bdew.advtech.registries.{MetalItemType, Metals}

object GenLangs {
  def main(args: Array[String]): Unit = {
    for (metal <- Metals.all) {
      if (metal.registerNugget) println(s"\"item.advtech.${metal.registryName(MetalItemType.Nugget)}\": \"${metal.name.capitalize} Nugget\",")
      if (metal.registerPlate) println(s"\"item.advtech.${metal.registryName(MetalItemType.Plate)}\": \"${metal.name.capitalize} Plate\",")
      if (metal.registerGear) println(s"\"item.advtech.${metal.registryName(MetalItemType.Gear)}\": \"${metal.name.capitalize} Gear\",")
      if (metal.registerIngot) println(s"\"item.advtech.${metal.registryName(MetalItemType.Ingot)}\": \"${metal.name.capitalize} Ingot\",")
      if (metal.registerBlock) println(s"\"block.advtech.${metal.registryName(MetalItemType.StorageBlock)}\": \"Block of ${metal.name.capitalize}\",")
      if (metal.registerOre) {
        println(s"\"block.advtech.${metal.registryName(MetalItemType.OreNormal)}\": \"${metal.name.capitalize} Ore\",")
        println(s"\"block.advtech.${metal.registryName(MetalItemType.OreDeep)}\": \"Deepslate ${metal.name.capitalize} Ore\",")
        println(s"\"block.advtech.${metal.registryName(MetalItemType.RawBlock)}\": \"Block of Raw ${metal.name.capitalize}\",")
        println(s"\"item.advtech.${metal.registryName(MetalItemType.RawDrop)}\": \"Raw ${metal.name.capitalize}\",")
      }
      if (metal.registerProcessing) {
        println(s"\"item.advtech.${metal.registryName(MetalItemType.Dust)}\": \"${metal.name.capitalize} Dust\",")
        println(s"\"item.advtech.${metal.registryName(MetalItemType.Chunks)}\": \"${metal.name.capitalize} Chunks\",")
        println(s"\"item.advtech.${metal.registryName(MetalItemType.Powder)}\": \"${metal.name.capitalize} Powder\",")
      }
    }
  }
}
