package system

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._

class ClockGenTest extends FreeSpec with ChiselScalatestTester {
  "generate a correct clock signal" in {
    test(new ClockGen(36)) {dut =>
      dut.io.ph0In.expect(false.B)
      dut.io.act.expect(false.B)
      dut.io.singleClock.poke(false.B)
      dut.io.singleClockSignal.poke(false.B)
      dut.clock.step(18)
      dut.io.ph0In.expect(true.B)
      dut.io.act.expect(true.B)
      dut.clock.step(1)
      dut.io.ph0In.expect(true.B)
      dut.io.act.expect(false.B)
      dut.clock.step(17)
      dut.io.ph0In.expect(false.B)
      dut.clock.step(1)
    }
  }

  "single clocks correctly" in {
    test(new ClockGen(36)) {dut =>
      dut.io.ph0In.expect(false.B)
      dut.io.act.expect(false.B)
      dut.io.singleClock.poke(true.B)
      dut.io.singleClockSignal.poke(false.B)
      dut.clock.step(1)
      dut.io.singleClockSignal.poke(true.B)
      dut.clock.step(1)
      dut.io.ph0In.expect(true.B)
      dut.io.act.expect(true.B)
      dut.io.singleClockSignal.poke(false.B)
      dut.clock.step(1)
      dut.io.act.expect(false.B)
      dut.io.ph0In.expect(true.B)
      dut.io.act.expect(false.B)
      dut.clock.step(1)
      dut.io.singleClockSignal.poke(true.B)      
      dut.io.act.expect(false.B)
      dut.clock.step(1)
      dut.io.ph0In.expect(false.B)
      dut.io.act.expect(false.B)
      dut.clock.step(1)
      dut.io.singleClockSignal.poke(false.B)
      dut.clock.step(1)
      dut.io.singleClockSignal.poke(true.B)      
      dut.clock.step(1)
      dut.io.act.expect(true.B)
      dut.io.ph0In.expect(true.B)
      dut.clock.step(1)
    }
  }

}
