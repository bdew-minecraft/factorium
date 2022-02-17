package net.bdew.advtech.misc

object AutoIOMode extends Enumeration {
  val NONE: Value = Value(0, "NONE")
  val INPUT: Value = Value(1, "INPUT")
  val OUTPUT: Value = Value(2, "OUTPUT")
  val BOTH: Value = Value(3, "BOTH")

  def canInput(v: Value): Boolean = v == INPUT || v == BOTH
  def canOutput(v: Value): Boolean = v == OUTPUT || v == BOTH
}

