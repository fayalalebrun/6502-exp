package system

import chisel3._
import chisel3.util._
import chisel3.experimental.chiselName

@chiselName
class ClockGen(dividerValue: Int) extends Module{
  val io = IO(new Bundle{
    val ph0In = Output(Bool())
    val act = Output(Bool())
    val singleClock = Input(Bool())
    val singleClockSignal = Input(Bool())
  });

  require(dividerValue%2==0)
  require(dividerValue>0)

  val ph0In = RegInit(false.B)

  val dividerCounter = new Counter(dividerValue/2)

  val singleClockPressed = RegInit(false.B)
  val singleClockAct = RegInit(false.B)//Wire(Bool())

  singleClockAct := io.singleClockSignal && !singleClockPressed && !ph0In // Only trigger on the positive edge

  when(!io.singleClock){
    when(dividerCounter.inc()){
      ph0In := !ph0In
    }
  }.otherwise {    
    when(io.singleClockSignal && !singleClockPressed){      
      ph0In := !ph0In
      singleClockPressed := true.B      
    }.elsewhen(!io.singleClockSignal && singleClockPressed){
      singleClockPressed := false.B
    }
  }


  io.ph0In := ph0In
  io.act := ((dividerCounter.value === 0.U) && ph0In && !io.singleClock) || singleClockAct
}
