package system

import chisel3._
import chisel3.util._
import chisel3.experimental._
import blackbox._


class UartToSound extends RawModule {
  val io = IO(new Bundle{
    val clk12 = Input(Clock())
    val reset = Input(Bool())
    val rx = Input(Bool())
  })

  val pll = Module(new LatticePLL)
  val fifo = Module(new LatticeFIFO)   
  val rs232_rx = Module(new rs232_rx)

  pll.io.CLKI := io.clk12

  rs232_rx.io.clk := pll.io.CLKOP
  rs232_rx.io.rst := io.reset
  rs232_rx.io.rx := io.rx
  rs232_rx.io.fifo_full := fifo.io.Full
  rs232_rx.io.fifo_data := fifo.io.Data

  fifo.io.WrClock:= pll.io.CLKOP
  fifo.io.WrEn := rs232_rx.io.fifo_WrEn

  
}
