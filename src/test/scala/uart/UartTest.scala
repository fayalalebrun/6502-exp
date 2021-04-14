package uart

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._

class UartTest extends FreeSpec with ChiselScalatestTester {
  "transmits correctly" in {
    test(new Tx(10000, 3000)){dut=>
      dut.clock.step(2)
      dut.io.channel.valid.poke(true.B)
      dut.io.channel.bits.poke('A'.U)

      dut.clock.step(2)
      
      dut.io.channel.valid.poke(false.B)
      dut.io.channel.bits.poke(0.U)

      dut.clock.step(3)

      for (i <- 0 until 8) {
        dut.io.txd.expect((('A'.toInt >> i) & 0x01).U)
       
        dut.clock.step(3)
      }

      dut.io.txd.expect(1.U)
    }
  }
  "receives correctly" in {
    test(new Rx(10000,3000)){dut =>

      dut.io.rxd.poke(1.U)
      dut.clock.step(10)

      dut.io.rxd.poke(0.U)
      dut.clock.step(3)

      for (i <- 0 until 8) {
        dut.io.rxd.poke(((0xa5.toInt >> i) & 0x01).U)
        dut.clock.step(3)
      }

      dut.io.rxd.poke(1.U)
      while(dut.io.channel.valid.peek().litValue == 0) {
 
        dut.clock.step(1)
      }
      dut.io.channel.bits.expect(0xa5.U)

 
      dut.io.channel.ready.poke(true.B)
      dut.clock.step(1)
      dut.io.channel.ready.poke(false.B)
      dut.clock.step(5)
    }
  }
}
