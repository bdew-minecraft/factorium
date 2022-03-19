package net.bdew.factorium.machines

import net.bdew.factorium.registries.Items
import net.bdew.lib.Text
import net.bdew.lib.keepdata.BlockItemKeepData
import net.minecraft.ChatFormatting
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.{ItemStack, TooltipFlag}
import net.minecraft.world.level.Level
import net.minecraftforge.fluids.FluidStack

import java.util

class BaseMachineItem(block: BaseMachineBlock[_]) extends BlockItemKeepData(block, Items.props) {
  override def appendHoverText(stack: ItemStack, world: Level, tip: util.List[Component], flag: TooltipFlag): Unit = {
    super.appendHoverText(stack, world, tip, flag)
    tip.add(Text.translate(getDescriptionId + ".desc").withStyle(ChatFormatting.GRAY))
    if (stack.hasTag && stack.getTag.contains("data", Tag.TAG_COMPOUND)) {
      val data = stack.getTag.getCompound("data")
      if (data.contains("power", Tag.TAG_COMPOUND)) {
        val power = data.getCompound("power").getFloat("stored")
        if (power > 0)
          tip.add(Text.energy(power).withStyle(ChatFormatting.YELLOW))
      }
      if (data.contains("tank", Tag.TAG_COMPOUND)) {
        val stack = FluidStack.loadFluidStackFromNBT(data.getCompound("tank"))
        if (!stack.isEmpty) {
          tip.add(Text.fluid(stack.getAmount).append(" ").append(stack.getDisplayName).withStyle(ChatFormatting.YELLOW))
        }
      }
      if (data.contains("inv", Tag.TAG_LIST)) {
        val items = data.getList("inv", Tag.TAG_COMPOUND).size()
        if (items > 0)
          tip.add(Text.translate("factorium.tip.items", items.toString).withStyle(ChatFormatting.YELLOW))
      }
      if (data.contains("upgrades", Tag.TAG_LIST)) {
        val items = data.getList("upgrades", Tag.TAG_COMPOUND).size()
        if (items > 0)
          tip.add(Text.translate("factorium.tip.upgrades", items.toString).withStyle(ChatFormatting.YELLOW))
      }
    }
  }
}
