package net.bdew.advtech.registries

import net.bdew.advtech.AdvTech
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraftforge.registries.ForgeRegistries

case class MetalEntry(name: String,
                      registerOre: Boolean = true,
                      registerIngot: Boolean = true,
                      registerGear: Boolean = true,
                      registerPlate: Boolean = true,
                      registerNugget: Boolean = true,
                      registerBlock: Boolean = true,
                      registerProcessing: Boolean = true,
                     ) {

  def registryName(kind: MetalItemType) = s"mat_${name}_${kind.kind}"
  def registryKey(kind: MetalItemType) = new ResourceLocation(AdvTech.ModId, registryName(kind))
  def item(kind: MetalItemType): Item = ForgeRegistries.ITEMS.getValue(registryKey(kind))
  def block(kind: MetalItemType): Block = ForgeRegistries.BLOCKS.getValue(registryKey(kind))
}

object MetalEntry {
  def Vanilla(name: String,
              registerGear: Boolean = true,
              registerPlate: Boolean = true,
              registerProcessing: Boolean = true,
             ): MetalEntry =
    MetalEntry(name,
      registerOre = false,
      registerIngot = false,
      registerNugget = false,
      registerBlock = false,
      registerGear = registerGear,
      registerPlate = registerPlate,
      registerProcessing = registerProcessing
    )
  def Alloy(name: String,
            registerGear: Boolean = true,
            registerPlate: Boolean = true,
           ): MetalEntry =
    MetalEntry(name,
      registerOre = false,
      registerIngot = false,
      registerProcessing = false,
      registerNugget = true,
      registerBlock = true,
      registerGear = registerGear,
      registerPlate = registerPlate,
    )
}

case class MetalItemType(kind: String)

object MetalItemType {
  val Ingot: MetalItemType = MetalItemType("ingot")
  val Gear: MetalItemType = MetalItemType("gear")
  val Plate: MetalItemType = MetalItemType("plate")
  val Nugget: MetalItemType = MetalItemType("nugget")
  val OreNormal: MetalItemType = MetalItemType("ore")
  val OreDeep: MetalItemType = MetalItemType("ore_deep")
  val RawDrop: MetalItemType = MetalItemType("raw")
  val RawBlock: MetalItemType = MetalItemType("raw_block")
  val StorageBlock: MetalItemType = MetalItemType("block")
  val Dust: MetalItemType = MetalItemType("dust")
  val Chunks: MetalItemType = MetalItemType("chunks")
  val Powder: MetalItemType = MetalItemType("powder")
}

object Metals {
  val tin: MetalEntry = MetalEntry("tin")

  val all = List(
    MetalEntry.Vanilla("iron"),
    MetalEntry.Vanilla("gold"),
    MetalEntry.Vanilla("copper"),
    tin,
    MetalEntry.Alloy("bronze"),
  )
}
