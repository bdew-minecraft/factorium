package net.bdew.advtech.machines.processing.smelter

import net.bdew.advtech.Config
import net.bdew.advtech.machines.MachineRecipes
import net.bdew.advtech.machines.processing.ProcessingMachineEntity
import net.bdew.advtech.registries.Blocks
import net.bdew.lib.Text
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class SmelterEntity(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends ProcessingMachineEntity(teType, pos, state) {
  override def config: SmelterConfig = Config.Machines.Smelter
  override def recipes: Set[SmelterRecipe] = MachineRecipes.smelter

  override def getDisplayName: Component = Text.translate(Blocks.smelter.block.get().getDescriptionId)
  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new SmelterContainer(this, playerInventory, id)
}
