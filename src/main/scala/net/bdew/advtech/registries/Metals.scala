package net.bdew.advtech.registries

case class MetalEntry(name: String,
                      registerOre: Boolean = true,
                      registerIngot: Boolean = true,
                      registerGear: Boolean = true,
                      registerPlate: Boolean = true,
                      registerNugget: Boolean = true,
                      registerBlock: Boolean = true,
                      registerProcessing: Boolean = true,
                     )

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

object Metals {
  val all = List(
    MetalEntry.Vanilla("iron"),
    MetalEntry.Vanilla("gold"),
    MetalEntry.Vanilla("copper"),
    MetalEntry("tin"),
    MetalEntry.Alloy("bronze"),
  )
}
