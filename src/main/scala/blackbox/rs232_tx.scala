package blackbox

import chisel3._
import chisel3.util._

class rs232_tx extends BlackBox {
  val io = IO(new Bundle{
    val clk = Input(Clock())
    val rst = Input(Bool())
    val tx = Output(Bool())
    val fifo_empty = Input(Bool())
    val fifo_RdEn = Output(Bool())
    val fifo_RdClock = Output(Bool())
    val fifo_data = Input(UInt(8.W))
  })
}
