package uart

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._



class UartDriverTest extends FreeSpec with ChiselScalatestTester {
  "Write data to uart fifo" in {
    test (new UartDriver(14)){ dut =>
      dut.io.routerBundle.selB.poke(true.B)
      dut.io.routerBundle.readWriteB.poke(true.B)
      dut.io.routerBundle.act.poke(false.B)
      dut.clock.step(1)
      dut.io.routerBundle.selB.poke(false.B)
      dut.io.routerBundle.readWriteB.poke(false.B)
      dut.io.routerBundle.act.poke(true.B)
      dut.io.routerBundle.address.poke(0x00.U)
      dut.io.routerBundle.dataIn.poke(0x55.U)
      dut.clock.step(1)
      dut.io.txData.valid.expect(true.B)
      dut.io.txData.bits.expect(0x55.U)
      dut.clock.step(1)
    }
  }
}
