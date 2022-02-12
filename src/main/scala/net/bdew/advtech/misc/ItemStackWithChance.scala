package net.bdew.advtech.misc

import com.google.gson.JsonObject
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.ShapedRecipe

import scala.util.Random

class ItemStackWithChance(val stack: ItemStack, val chance: Float) {
  def toNetwork(buffer: FriendlyByteBuf): Unit = {
    buffer.writeItem(stack)
    buffer.writeFloat(chance)
  }

  def isEmpty: Boolean = stack.isEmpty || chance <= 0
  def nonEmpty: Boolean = !isEmpty

  def roll(count: Int): ItemStack = {
    if (isEmpty) return ItemStack.EMPTY

    val rolls =
      if (chance == 1)
        count
      else if (count == 1)
        if (Random.nextFloat() <= chance) 1 else 0
      else
        (0 until count).map(_ => if (Random.nextFloat() <= chance) 1 else 0).sum

    val res = stack.copy()
    res.setCount(rolls * res.getCount)
    res
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