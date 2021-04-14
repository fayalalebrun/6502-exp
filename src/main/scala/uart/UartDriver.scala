package uart

import chisel3._
import chisel3.util._

import blackbox.LFifoWriterBundle
import blackbox.LFifoReaderBundle
import system.RouterBundle

class UartDriver(addressWidth: Int) extends Module {
  val io = IO(new Bundle{
    val routerBundle = new RouterBundle(addressWidth)
    val txData = Decoupled(UInt(8.W))
    val rxData = Flipped(Decoupled(UInt(8.W)))
  })


  io.txData.valid := false.B


  io.rxData.ready := false.B

  

  val enable_write = Wire(Bool())
  enable_write := false.B

  val txDataOut = Reg(UInt(8.W))
  val dataOut = RegInit(0xFF.U(8.W))

  io.routerBundle.dataOut := dataOut

  when (!io.routerBundle.selB&&io.routerBundle.act){
    when(!io.routerBundle.readWriteB){
      switch(io.routerBundle.address){
        is(0x0.U){ // Data Register - Write
          txDataOut := io.routerBundle.dataIn
          enable_write := true.B
        }
      }
    }.otherwise {
      switch(io.routerBundle.address){
        is(0x0.U){ // Data register - Read
          when(io.rxData.valid) {
            io.rxData.ready := true.B
            dataOut := io.rxData.bits
          }.otherwise {
            dataOut := 0xFF.U
          }
        }
        is(0x1.U){ // Status register - Read
          dataOut := Cat(Seq(Fill(3,false.B),io.txData.ready,io.rxData.valid,  Fill(3,false.B)))
        }
      }
    }
  }



  io.txData.valid := ShiftRegister(enable_write, 1)
  io.txData.bits := txDataOut

}
