package sound

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselStage, ChiselGeneratorAnnotation}


class SoundController (width : Int = 8, clockFreq: Int = 12000000) extends Module {
  val io = IO(new Bundle{
    val spiOut = Output(new Mcp49xxSpiOutputBundle)
    val fifoQ = Input(UInt(width.W))
    val fifoReadEnable = Output(Bool())
    val fifoEmpty = Input(Bool())
  })

  val spiControl = Module(new Mcp49xxSpi(width=width))






  val valid = RegInit(false.B)
  val readEnable = RegInit(false.B)

  

  when(io.fifoEmpty === false.B){
    valid := true.B
    readEnable := true.B
  }

  when(readEnable){
    readEnable := false.B
  }

  when(spiControl.io.data.ready && valid === true.B){
    valid := false.B    
  }

  spiControl.io.data.valid := ShiftRegister(valid,1)
  spiControl.io.data.bits := io.fifoQ
  io.spiOut := spiControl.io.out
  io.fifoReadEnable := readEnable
}
