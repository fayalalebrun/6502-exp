package uart

import chisel3._
import chisel3.util._
import chisel3.tester._
import org.scalatest.FreeSpec
import chisel3.experimental.BundleLiterals._


class QueueTest extends FreeSpec with ChiselScalatestTester {
  "Check queue functionality" in {
    test (new Queue(UInt(8.W), 32)){ dut =>
      dut.io.enq.valid.poke(false.B)
      dut.io.deq.ready.poke(false.B)
      dut.clock.step(1)
      dut.io.enq.bits.poke(255.U)
      dut.io.enq.valid.poke(true.B)
      dut.clock.step(1)
      dut.io.enq.bits.poke(32.U)
      dut.io.enq.valid.poke(true.B)
      dut.clock.step(1)
      dut.io.enq.valid.poke(false.B)
      dut.clock.step(1)
      dut.io.deq.ready.poke(true.B)
      dut.io.deq.bits.expect(255.U)
      dut.io.deq.valid.expect(true.B)
      dut.clock.step(1)
      dut.io.deq.ready.poke(false.B)
      dut.io.deq.bits.expect(32.U)
      dut.clock.step(1)
      dut.io.deq.bits.expect(32.U)
      dut.clock.step(1)
      dut.io.deq.bits.expect(32.U)
      dut.clock.step(1)
      dut.io.deq.bits.expect(32.U)
      dut.clock.step(1)
    }

  }
}
