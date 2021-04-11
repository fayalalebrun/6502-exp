package sound

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._


class SoundControllerTest extends FreeSpec with ChiselScalatestTester {
  "transmit data in the correct intervals" in {
    test(new SoundController(clockFreq=100)) { dut =>
      dut.io.fifoQ.poke(8.U)
      dut.io.fifoEmpty.poke(true.B)
      dut.io.fifoReadEnable.expect(false.B)
      dut.io.spiOut.cs.expect(true.B)
      dut.clock.step(1)
      dut.io.fifoReadEnable.expect(false.B)
      dut.io.spiOut.cs.expect(true.B)
      dut.io.fifoEmpty.poke(false.B)
      dut.clock.step(1)
      dut.io.fifoEmpty.poke(true.B)
      dut.io.fifoReadEnable.expect(true.B)      
      dut.io.spiOut.cs.expect(true.B)
      dut.clock.step(1)
      dut.io.fifoEmpty.poke(true.B)
      dut.clock.step(1)
      dut.io.fifoReadEnable.expect(false.B)
      dut.io.spiOut.cs.expect(false.B)
      dut.clock.step(1)
    }

  }

}
