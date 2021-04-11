package system

import chisel3._
import chisel3.util._
import blackbox._
import sound._


class UartToSound extends RawModule {
  val io = IO(new Bundle{
    val clk12 = Input(Clock())
    val reset = Input(Bool())
    val rx = Input(Bool())
    val spiOut = Output(new Mcp49xxSpiOutputBundle)    
  })

  val pll = Module(new LatticePLL)
  val fifo = Module(new LatticeFIFO)   
  val rs232_rx = Module(new rs232_rx)

  val resetComp = Wire(Bool())

  resetComp := !io.reset // The button is 1 when pressed

  pll.io.CLKI := io.clk12

  rs232_rx.io.clk := pll.io.CLKOP
  rs232_rx.io.rst := resetComp
  rs232_rx.io.rx := io.rx
  rs232_rx.io.fifo_full := fifo.io.Full
  
  fifo.io.Data := rs232_rx.io.fifo_data
  fifo.io.WrClock:= pll.io.CLKOP
  fifo.io.WrEn := rs232_rx.io.fifo_WrEn

  withClockAndReset(pll.io.CLKOS,resetComp){
    val soundController = Module(new SoundController)
    soundController.io.fifoQ := fifo.io.Q
    soundController.io.fifoEmpty := fifo.io.Empty

    io.spiOut := soundController.io.spiOut

    fifo.io.RdEn := soundController.io.fifoReadEnable
    fifo.io.RdClock := pll.io.CLKOS
  }

  fifo.io.Reset := resetComp
  
}

object UartToSound extends App {
  chisel3.Driver.execute(args, () => new UartToSound())
}
