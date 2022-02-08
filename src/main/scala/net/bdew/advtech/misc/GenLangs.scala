package net.bdew.advtech.misc

import net.bdew.advtech.registries.Metals

object GenLangs {
  def main(args: Array[String]): Unit = {
    for (metal <- Metals.all) {
      if (metal.registerNugget) println(s"\"item.advtech.mat_${metal.name}_nugget\": \"${metal.name.capitalize} nugget\",")
      if (metal.registerPlate) println(s"\"item.advtech.mat_${metal.name}_plate\": \"${metal.name.capitalize} plate\",")
      if (metal.registerGear) println(s"\"item.advtech.mat_${metal.name}_gear\": \"${metal.name.capitalize} gear\",")
      if (metal.registerIngot) println(s"\"item.advtech.mat_${metal.name}_ingot\": \"${metal.name.capitalize} ingot\",")
      if (metal.registerOre) println(s"\"block.advtech.mat_${metal.name}_ore\": \"${metal.name.capitalize} ore\",")
      if (metal.registerBlock) println(s"\"block.advtech.mat_${metal.name}_block\": \"${metal.name.capitalize} block\",")
      if (metal.registerProcessing) {
        println(s"\"item.advtech.mat_${metal.name}_dust\": \"${metal.name.capitalize} dust\",")
        println(s"\"item.advtech.mat_${metal.name}_chunks\": \"${metal.name.capitalize} chunks\",")
        println(s"\"item.advtech.mat_${metal.name}_powder\": \"${metal.name.capitalize} powder\",")
      }
    }
  }
}
