package net.bdew.factorium.machines.processing.pulverizer

import net.bdew.factorium.Config
import net.bdew.factorium.machines.MachineRecipes
import net.bdew.factorium.machines.processing.ProcessingMachineEntity
import net.bdew.factorium.registries.Blocks
import net.bdew.lib.Text
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class PulverizerEntity(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends ProcessingMachineEntity(teType, pos, state) {
  override def config: PulverizerConfig = Config.Machines.Pulverizer
  override def recipes: Set[PulverizerRecipe] = MachineRecipes.pulverizer

  override def getDisplayName: Component = Text.translate(Blocks.pulverizer.block.get().getDescriptionId)
  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new PulverizerContainer(this, playerInventory, id)
}
