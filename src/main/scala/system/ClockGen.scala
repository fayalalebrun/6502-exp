package system

import chisel3._
import chisel3.util._

class ClockGen(clockFreq: Int, desiredFreq: Int) extends Module{
  val io = IO(new Bundle{
    val ph0In = Output(Bool())
  });
  val dividerVal = (clockFreq/(desiredFreq*2))
  require(clockFreq>=desiredFreq*2)
  require(clockFreq%dividerVal==0)
  

  val ph0In = RegInit(true.B)

  val dividerCounter = new Counter(dividerVal)

  when(dividerCounter.inc()){
    ph0In := !ph0In
  }


  io.ph0In := ph0In
}
