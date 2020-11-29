package blackbox

import chisel3._
import chisel3.util._

class LatticePLL extends BlackBox {
  val io = IO(new Bundle{
    val CLKI = Input(Clock())
    val CLKOP = Output(Clock())
    val CLKOS = Output(Clock())
    val CLKOS2 = Output(Clock())
    val CLKOS3 = Output(Clock())
  })
}
