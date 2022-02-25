package net.bdew.advtech.misc

import net.bdew.advtech.AdvTech
import net.bdew.advtech.metals.{MetalItemType, Metals}

object GenLangs {
  def main(args: Array[String]): Unit = {
    val out = List.newBuilder[(String, String)]
    val kinds = Metals.all.flatMap(_.items.keySet).toSet
    for (kind <- kinds) {
      val key = s"item.${AdvTech.ModId}.metal.${kind.kind}"
      val mname = "%s"
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
        case x => //
      }
    }
    println(out.result().sortBy(_._1).map(x => s"\"${x._1}\": \"${x._2}\",").mkString("\n"))
  }
}
