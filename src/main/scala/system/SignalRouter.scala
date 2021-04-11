package system

import chisel3._
import chisel3.util._

import uart.UartModule

class RouterBundle(addressWidth: Int) extends Bundle {
  val address = Input(UInt(addressWidth.W))
  val selB = Input(Bool())
  val readWriteB = Input(Bool())
  val dataIn = Input(UInt(8.W))
  val dataOut = Output(UInt(8.W))
  val act = Input(Bool())
}

class SignalRouter(addressWidth: Int, clockFreq: Int) extends Module{

  val io = IO(new Bundle{
    val bundle = new RouterBundle(addressWidth)
    val irqB = Output(Bool())
    val uartTx = Output(Bool())
    val uartRx = Input(Bool())

    val uartClock = Input(Clock())
  });

  val uartModule = Module(new UartModule(addressWidth))
  uartModule.io.routerBundle <> io.bundle
  io.uartTx := uartModule.io.tx
  uartModule.io.rx := io.uartRx

  io.irqB := true.B
}
