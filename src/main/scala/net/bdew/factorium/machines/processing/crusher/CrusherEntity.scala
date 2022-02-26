package net.bdew.factorium.machines.processing.crusher

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

class CrusherEntity(teType: BlockEntityType[_], pos: BlockPos, state: BlockState) extends ProcessingMachineEntity(teType, pos, state) {
  override def config: CrusherConfig = Config.Machines.Crusher
  override def recipes: Set[CrusherRecipe] = MachineRecipes.crusher

  override def getDisplayName: Component = Text.translate(Blocks.crusher.block.get().getDescriptionId)
  override def createMenu(id: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
    new CrusherContainer(this, playerInventory, id)
}
