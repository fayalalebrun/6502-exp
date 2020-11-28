package sound

import chisel3._
import chisel3.util._

class Mcp49xxSpiOutputBundle extends Bundle{
  val cs = Bool()
  val sdi = Bool()
  val sck = Bool()
}

class Mcp49xxSpi(width: Int = 8, buffered: Boolean = false, gain: Boolean = true, shutdown: Boolean = true) extends Module {
  val io = IO(new Bundle{
    val data = Flipped(Decoupled(UInt(width.W)))
    val out = Output(new Mcp49xxSpiOutputBundle)
  })

  val busy = RegInit(false.B)

  val latchData = Reg(UInt(width.W))


  val counterModule = new Counter(16)

  val sck = RegInit(false.B)



  io.data.ready := !busy

  io.out.sdi := true.B
  io.out.cs := true.B
  io.out.sck := sck
  sck := false.B
  when(!busy&&io.data.valid){
    busy := true.B
    latchData := io.data.bits

  }.elsewhen(busy){
    when(sck===true.B){
      when(counterModule.inc()){busy:= false.B}
    }


    sck := !sck
    io.out.cs := false.B

    when(counterModule.value <= 3.U){
      switch(counterModule.value){
        is(0.U){
          io.out.sdi := false.B
        }
        is(1.U){
          io.out.sdi := buffered.B
        }
        is(2.U){
          io.out.sdi := gain.B
        }
        is(3.U){
          io.out.sdi := shutdown.B
        }
      }
    }.elsewhen(counterModule.value - 4.U < width.U){
      io.out.sdi := io.data.bits(width.U - 1.U -(counterModule.value - 4.U))
    }

  }

}
