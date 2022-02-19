package net.bdew.advtech.machines

import net.bdew.advtech.machines.alloy.AlloyRecipe
import net.bdew.advtech.machines.processing.crusher.CrusherRecipe
import net.bdew.advtech.machines.processing.grinder.GrinderRecipe
import net.bdew.advtech.machines.processing.pulverizer.PulverizerRecipe
import net.bdew.advtech.machines.processing.smelter.SmelterRecipe
import net.bdew.advtech.misc.ItemStackWithChance
import net.bdew.advtech.registries.Recipes
import net.bdew.lib.recipes.RecipeReloadListener
import net.minecraft.world.Container
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.{RecipeManager, RecipeType, SmeltingRecipe}
import net.minecraftforge.common.Tags

import scala.jdk.CollectionConverters._

object MachineRecipes {
  var crusher = Set.empty[CrusherRecipe]
  var grinder = Set.empty[GrinderRecipe]
  var pulverizer = Set.empty[PulverizerRecipe]
  var smelter = Set.empty[SmelterRecipe]
  var alloy = Set.empty[AlloyRecipe]

  private def makeVanillaAdaptedRecipes(mgr: RecipeManager): (List[CrusherRecipe], List[SmelterRecipe]) = {
    val (crusher, smelter) =
      mgr.getAllRecipesFor[Container, SmeltingRecipe](RecipeType.SMELTING)
        .asScala.toList
        .partition(rec =>
          rec.getIngredients.get(0).getItems.forall(_.is(Tags.Items.ORES))
            && !rec.getResultItem.is(Tags.Items.INGOTS)
        )

    val crusherAdapted = crusher.map(rec => {
      val stack = rec.getResultItem.copy()
      stack.setCount(stack.getCount * 2)
      new CrusherRecipe(rec.getId,
        rec.getIngredients.get(0),
        ItemStackWithChance(stack),
        ItemStackWithChance.from(Items.GRAVEL, chance = 0.1f),
        ItemStackWithChance.EMPTY
      )
    })

    val smelterAdapted = smelter.map(rec => new SmelterRecipe(rec.getId,
      rec.getIngredients.get(0),
      ItemStackWithChance(rec.getResultItem),
      ItemStackWithChance.EMPTY,
      ItemStackWithChance.EMPTY
    ))

    (crusherAdapted, smelterAdapted)
  }

  def refreshRecipes(manager: RecipeManager): Unit = {
    val (crusherAdapted, smelterAdapted) = makeVanillaAdaptedRecipes(manager)
    crusher = (Recipes.crusherType.getAllRecipes(manager) ++ crusherAdapted).toSet
    smelter = (Recipes.smelterType.getAllRecipes(manager) ++ smelterAdapted).toSet
    grinder = Recipes.grinderType.getAllRecipes(manager).toSet
    pulverizer = Recipes.pulverizerType.getAllRecipes(manager).toSet
    alloy = Recipes.alloyType.getAllRecipes(manager).toSet
  }

  def init(): Unit = {
    RecipeReloadListener.onServerRecipeUpdate.listen(refreshRecipes)
    RecipeReloadListener.onClientRecipeUpdate.listen(refreshRecipes)
  }
}
