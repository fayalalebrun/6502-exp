package sound

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._


class SoundControllerTest extends FreeSpec with ChiselScalatestTester {
  "transmit data in the correct intervals" in {
    test(new SoundController(desiredFreq=2, clockFreq=100)) { dut =>
      dut.clock.setTimeout(0)
      fork{
        for(i <- 0 to 255){
          dut.clock.step(50)
          
          dut.io.spi_out.cs.expect(true.B)
        }
      }.join()

    }

  }

}
