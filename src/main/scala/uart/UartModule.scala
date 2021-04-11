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
  val rs232Tx = Module(new rs232_tx)

  rs232Tx.io.clk := clock
  rs232Tx.io.rst := reset.asBool()
  io.tx := rs232Tx.io.tx
  rs232Tx.io.fifo_empty := !txQueue.io.deq.valid
  txQueue.io.deq.ready := rs232Tx.io.fifo_RdEn
  rs232Tx.io.fifo_data := txQueue.io.deq.bits

  val rxQueue = Module(new Queue(UInt(), 32))
  val rs232Rx = Module(new rs232_rx)
  rs232Rx.io.clk := clock
  rs232Rx.io.rst := reset.asBool()
  rs232Rx.io.rx := io.rx
  rs232Rx.io.fifo_full := !rxQueue.io.enq.ready
  rxQueue.io.enq.valid := rs232Rx.io.fifo_WrEn
  rxQueue.io.enq.bits := rs232Rx.io.fifo_data
  
  uartDriver.io.routerBundle <> io.routerBundle
  uartDriver.io.txData <> txQueue.io.enq
  uartDriver.io.rxData <> rxQueue.io.deq
}
