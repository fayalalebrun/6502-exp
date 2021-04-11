package system

import chisel3._
import chisel3.util._
import chisel3.experimental.Analog

import blackbox._
import uart._




class Top(addressWidth: Int = 14, clockFreq: Int) extends RawModule {
  val io = IO(new Bundle{
    val address = Input(UInt(addressWidth.W))
    val data = Analog(8.W)
    val selB = Input(Bool())
    val readWriteB = Input(Bool())
    val irqB = Output(Bool())
    val resB = Output(Bool())
    val ph0In = Output(Bool())

    val clk12 = Input(Clock())
    val reset = Input(Bool())

    val uartTx = Output(Bool())
    val uartRx = Input(Bool())
  })

  val pll = Module(new LatticePLL)
  pll.io.CLKI := io.clk12
  

  val dataBus = Module(new DataBus)

  val writeOut = Wire(Bool())

  writeOut := !io.selB && io.readWriteB


  dataBus.io.dataio <> io.data
  dataBus.io.oe := writeOut 

  withClockAndReset(pll.io.CLKOP, !io.reset){
    val clockGen = Module(new ClockGen(clockFreq, clockFreq/32))
    val signalRouter = Module(new SignalRouter(addressWidth, clockFreq))

    io.ph0In := clockGen.io.ph0In

    signalRouter.io.bundle.address := io.address
    signalRouter.io.bundle.selB := io.selB
    signalRouter.io.bundle.readWriteB := io.readWriteB
    signalRouter.io.bundle.dataIn := dataBus.io.dataout
    dataBus.io.datain := signalRouter.io.bundle.dataOut
    signalRouter.io.bundle.act := clockGen.io.act
    io.irqB := signalRouter.io.irqB
    io.uartTx := signalRouter.io.uartTx
    signalRouter.io.uartClock := pll.io.CLKOP
    signalRouter.io.uartRx := io.uartRx

    io.resB := !io.reset
  }

}

object Top extends App {
  chisel3.Driver.execute(args, () => new Top(clockFreq=12000000))
}

class DataBus extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val dataout = Output(UInt(8.W))    
    val dataio = Analog(8.W)
    val datain = Input(UInt(8.W))
    val oe = Input(Bool())
  })
  setInline("DataBus.v",
    s"""
    |module DataBus(
    |     output [7:0] dataout,
    |     inout [7:0] dataio,
    |     input [7:0] datain,
    |     input oe);
    |
    |   
    |   
    |   assign dataio = oe ? datain : 8'bz;
    |   assign dataout = dataio;
    |   
    |   
    |endmodule
    """.stripMargin
  )
}
