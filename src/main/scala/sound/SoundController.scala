package sound

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselStage, ChiselGeneratorAnnotation}


class SoundController (width : Int = 8, desiredFreq : Int = 5000, clockFreq: Int = 12000000) extends Module {
  val io = IO(new Bundle{
    val spi_out = Output(new Mcp49xxSpiOutputBundle)
  })

  val spiControl = Module(new Mcp49xxSpi(width=width))

  val countTo = clockFreq/desiredFreq

  val freqDivCounter = new Counter(countTo)

  val numberCounter = new Counter(256)

  val bitsOut = Reg(UInt(width.W))
  val valid = RegInit(false.B)

  

  when(freqDivCounter.inc()){
    bitsOut := numberCounter.value
    valid := true.B
    
    numberCounter.inc()    
  }

  when(spiControl.io.data.ready && valid === true.B){
    valid := false.B
  }

  spiControl.io.data.valid := valid
  spiControl.io.data.bits := bitsOut
  io.spi_out := spiControl.io.out
}

object SoundController extends App {
  chisel3.Driver.execute(args, () => new SoundController(desiredFreq=200*256))
}
