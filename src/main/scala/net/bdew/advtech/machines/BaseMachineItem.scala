package net.bdew.advtech.machines

import net.bdew.advtech.registries.Items
import net.bdew.lib.keepdata.BlockItemKeepData

class BaseMachineItem(block: BaseMachineBlock[_]) extends BlockItemKeepData(block, Items.props) {

}
