package net.bdew.advtech.misc

import net.bdew.advtech.AdvTech
import net.bdew.advtech.metals.{MetalItemType, Metals}

object GenLangs {
  def main(args: Array[String]): Unit = {
    val out = List.newBuilder[(String, String)]
    for (metal <- Metals.all; kind <- metal.items.keySet ++ metal.blocks.keySet) {
      val mname = metal.name.capitalize
      if (metal.ownBlock(kind)) {
        val key = s"block.${AdvTech.ModId}.${metal.registryName(kind)}"
        kind match {
          case MetalItemType.OreNormal => out.addOne(key, s"$mname Ore")
          case MetalItemType.OreDeep => out.addOne(key, s"Deepslate $mname Ore")
          case MetalItemType.OreNether => out.addOne(key, s"Nether $mname Ore")
          case MetalItemType.OreEnd => out.addOne(key, s"Endstone $mname Ore")
          case MetalItemType.RawBlock => out.addOne(key, s"Block of Raw $mname")
          case MetalItemType.StorageBlock => out.addOne(key, s"Block of $mname")
          case x => throw new RuntimeException(s"Missing kind $x")
        }
      } else if (metal.ownItem(kind)) {
        val key = s"item.${AdvTech.ModId}.${metal.registryName(kind)}"
        kind match {
          case MetalItemType.RawDrop => out.addOne(key, s"Raw $mname")
          case MetalItemType.Nugget => out.addOne(key, s"$mname Nugget")
          case MetalItemType.Ingot => out.addOne(key, s"$mname Ingot")
          case MetalItemType.Dust => out.addOne(key, s"$mname Dust")
          case MetalItemType.Chunks => out.addOne(key, s"$mname Chunks")
          case MetalItemType.Powder => out.addOne(key, s"$mname Powder")
          case MetalItemType.Plate => out.addOne(key, s"$mname Plate")
          case MetalItemType.Gear => out.addOne(key, s"$mname Gear")
          case MetalItemType.Rod => out.addOne(key, s"$mname Rod")
          case MetalItemType.Wire => out.addOne(key, s"$mname Wire")
          case x => throw new RuntimeException(s"Missing kind $x")
        }
      }
    }
    println(out.result().sortBy(_._1).map(x => s"\"${x._1}\": \"${x._2}\",").mkString("\n"))
  }
}
