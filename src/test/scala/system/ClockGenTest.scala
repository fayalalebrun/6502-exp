package system

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._

class ClockGenTest extends FreeSpec with ChiselScalatestTester {
  "generate a correct clock signal" in {
    test(new ClockGen(16, 4)) {dut =>
      dut.io.ph0In.expect(true.B)
      dut.clock.step(1)
      dut.io.ph0In.expect(true.B)
      dut.clock.step(1)
      dut.io.ph0In.expect(false.B)
      dut.clock.step(2)
      dut.io.ph0In.expect(true.B)
      dut.clock.step(2)
    }
  }
}
