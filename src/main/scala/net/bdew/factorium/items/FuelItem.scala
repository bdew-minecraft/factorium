package net.bdew.factorium.items

import net.bdew.factorium.registries.Items
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.{Item, ItemStack}

class FuelItem(burnTime: Int) extends Item(Items.resourceProps) {
  override def getBurnTime(itemStack: ItemStack, recipeType: RecipeType[_]): Int = burnTime
}
