package net.bdew.advtech.misc

import com.google.gson.JsonObject
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.ShapedRecipe

class ItemStackWithChance(stack: ItemStack, chance: Float) {
  def toNetwork(buffer: FriendlyByteBuf): Unit = {
    buffer.writeItem(stack)
    buffer.writeFloat(chance)
  }
}

object ItemStackWithChance {
  val EMPTY = new ItemStackWithChance(ItemStack.EMPTY, 0)

  def fromNetwork(buffer: FriendlyByteBuf): ItemStackWithChance = {
    val stack = buffer.readItem()
    val chance = buffer.readFloat()
    new ItemStackWithChance(stack, chance)
  }

  def fromJson(obj: JsonObject): ItemStackWithChance = {
    val stack = ShapedRecipe.itemStackFromJson(obj)
    val chance = if (obj.has("chance")) obj.get("chance").getAsFloat else 1
    new ItemStackWithChance(stack, chance)
  }

  def fromJsonOpt(parent: JsonObject, name: String): ItemStackWithChance = {
    if (parent.has(name))
      fromJson(parent.getAsJsonObject(name))
    else
      EMPTY
  }
}