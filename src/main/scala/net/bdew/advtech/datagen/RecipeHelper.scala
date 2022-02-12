package net.bdew.advtech.datagen

import com.google.gson.JsonObject
import net.bdew.advtech.misc.ItemStackWithChance

object RecipeHelper {
  def writeItemStackWithChance(v: ItemStackWithChance): JsonObject = {
    val obj = new JsonObject
    obj.addProperty("item", v.stack.getItem.getRegistryName.toString)
    if (v.stack.getCount > 1)
      obj.addProperty("count", v.stack.getCount)
    if (v.chance != 1)
      obj.addProperty("chance", v.chance)
    obj
  }
}
