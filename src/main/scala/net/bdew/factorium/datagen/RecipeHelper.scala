package net.bdew.factorium.datagen

import com.google.gson.JsonObject
import net.bdew.factorium.misc.ItemStackWithChance
import net.minecraft.advancements.critereon.{EntityPredicate, InventoryChangeTrigger, ItemPredicate, MinMaxBounds}
import net.minecraft.tags.TagKey
import net.minecraft.world.item.{Item, ItemStack}
import net.minecraft.world.level.ItemLike

object RecipeHelper {
  def writeItemStack(v: ItemStack): JsonObject = {
    val obj = new JsonObject
    obj.addProperty("item", v.getItem.getRegistryName.toString)
    if (v.getCount > 1)
      obj.addProperty("count", v.getCount)
    obj
  }

  def writeItemStackWithChance(v: ItemStackWithChance): JsonObject = {
    val obj = new JsonObject
    obj.addProperty("item", v.stack.getItem.getRegistryName.toString)
    if (v.stack.getCount > 1)
      obj.addProperty("count", v.stack.getCount)
    if (v.chance != 1)
      obj.addProperty("chance", v.chance)
    obj
  }

  def inventoryTrigger(tests: ItemPredicate*): InventoryChangeTrigger.TriggerInstance = {
    new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, tests.toArray)
  }

  def has(tag: TagKey[Item]): InventoryChangeTrigger.TriggerInstance = {
    inventoryTrigger(ItemPredicate.Builder.item.of(tag).build)
  }

  def has(item: ItemLike): InventoryChangeTrigger.TriggerInstance = {
    inventoryTrigger(ItemPredicate.Builder.item.of(item).build)
  }
}
