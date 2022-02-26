package net.bdew.factorium.misc

import com.google.gson.JsonObject
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

case class IngredientMulti(ingredient: Ingredient, count: Int = 1) {
  def toNetwork(pkt: FriendlyByteBuf): Unit = {
    ingredient.toNetwork(pkt)
    pkt.writeInt(count)
  }

  def test(stack: ItemStack): Boolean = ingredient.test(stack) && stack.getCount >= count
  def testSoft(stack: ItemStack): Boolean = ingredient.test(stack)
}


object IngredientMulti {
  def fromNetwork(pkt: FriendlyByteBuf): IngredientMulti = {
    val ingredient = Ingredient.fromNetwork(pkt)
    val count = pkt.readInt()
    IngredientMulti(ingredient, count)
  }

  def fromJson(js: JsonObject): IngredientMulti = {
    val ingredient = Ingredient.fromJson(js)
    val count = if (js.has("count")) js.get("count").getAsInt else 1
    IngredientMulti(ingredient, count)
  }
}