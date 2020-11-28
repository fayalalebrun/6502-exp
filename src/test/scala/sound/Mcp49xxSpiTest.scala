package sound

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._


class Mcp49xxSpiTest extends FreeSpec with ChiselScalatestTester {
  "transmit the data given to it" in {
    test(new Mcp49xxSpi()) { dut =>
      val x = Seq.tabulate(256){i => i + 1}
      
      x.foreach{data =>
        dut.io.data.bits.poke(data.U)
        dut.io.data.valid.poke(true.B)        
        dut.io.data.ready.expect(true.B)
        dut.io.out.cs.expect(true.B)
        dut.clock.step(2)

        dut.io.data.valid.poke(false.B)
        dut.io.out.cs.expect(false.B)
        dut.io.data.ready.expect(false.B)
        dut.io.out.sdi.expect(false.B) // active
        dut.clock.step(2)

        dut.io.out.sdi.expect(false.B) // buffered
        dut.clock.step(2)

        dut.io.out.sdi.expect(true.B) // gain
        dut.clock.step(2)

        dut.io.out.sdi.expect(true.B) // shutdown
        dut.clock.step(2)

        for(i <- 7 to 0){
          dut.io.out.sdi.expect(((data >> i)&1).U.asBool)
          dut.clock.step(2)
        }
        dut.clock.step(24) // For some reason the above code is done in parallel?
      }
    }
  }
}
