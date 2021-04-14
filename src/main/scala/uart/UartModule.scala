package uart

import chisel3._
import chisel3.util._

import blackbox._
import system.RouterBundle

class UartModule(addressWidth: Int) extends Module{
  val io = IO(new Bundle{
    val routerBundle = new RouterBundle(addressWidth)
    val tx = Output(Bool());
    val rx = Input(Bool())
  })


  val uartDriver = Module(new UartDriver(addressWidth))


  val txQueue = Module(new Queue(UInt(), 32))
  val uartTx = Module(new Tx(36000000, 115200))

  io.tx := uartTx.io.txd
  txQueue.io.deq <> uartTx.io.channel


  val rxQueue = Module(new Queue(UInt(), 32))
  val uartRx = Module(new Rx(36000000, 115200))
  uartRx.io.rxd := io.rx
  uartRx.io.channel <> rxQueue.io.enq
  
  uartDriver.io.routerBundle <> io.routerBundle
  uartDriver.io.txData <> txQueue.io.enq
  uartDriver.io.rxData <> rxQueue.io.deq
}
