package system

import chisel3._
import chisel3.util._
import chisel3.experimental.Analog
import sound.Mcp49xxSpiOutputBundle
import sound.SoundController




class Top(addressWidth: Int = 14, clockFreq: Int) extends Module {
  val io = IO(new Bundle{
    val address = Input(UInt(addressWidth.W))
    val data = Analog(8.W)
    val selB = Input(Bool())
    val readWriteB = Input(Bool())
    val irqB = Output(Bool())
    val resB = Output(Bool())
    val ph0In = Output(Bool())
    val soundSpiOut = Output(new Mcp49xxSpiOutputBundle)
  })
  val soundController = Module(new SoundController(clockFreq = clockFreq))
  io.soundSpiOut := soundController.io.spi_out

  val clockGen = Module(new ClockGen(clockFreq, clockFreq/2))
  io.ph0In := clockGen.io.ph0In

  val dataBusInOut = Module(new DataBusInOut)
  dataBusInOut.io.dataio <> io.data
  dataBusInOut.io.oe := true.B // TODO
  dataBusInOut.io.datain := 0xEA.U

  //TODO section
  io.irqB := true.B
  io.resB := true.B
}

object Top extends App {
  chisel3.Driver.execute(args, () => new Top(clockFreq=12000000))
}

class DataBusInOut extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val datain = Input(UInt(8.W))
    val dataout = Output(UInt(8.W))
    val dataio = Analog(8.W)
    val oe = Input(Bool())
  })
  setInline("DataBus.v",
    s"""
    |module DataBus(
    |     output [7:0] dataout,
    |     inout [7:0] dataio,
    |     input [7:0] datain,
    |     input oen);
    |
    |   assign dataio = (oen == 'b0) ? datain : 'bzzzzzzzz;
    |   assign dataout = dataio;
    |endmodule
    """.stripMargin
  )
}
