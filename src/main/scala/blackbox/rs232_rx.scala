package blackbox

import chisel3._
import chisel3.util._

class rs232_rx extends BlackBox {
  val io = IO(new Bundle{
    val clk = Input(Clock())
    val rst = Input(Bool())
    val rx = Input(Bool())
    val fifo_full = Input(Bool())
    val fifo_WrEn = Output(Bool())
    val fifo_WrClock = Output(Clock())
    val fifo_data = Output(UInt(8.W))
  })
}
