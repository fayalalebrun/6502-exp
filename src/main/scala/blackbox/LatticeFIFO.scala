package blackbox

import chisel3._
import chisel3.util._

class LFifoWriterBundle extends Bundle {
  val Data = Input(UInt(8.W))
  val WrClock = Input(Clock())
  val WrEn = Input(Bool())
  val Full = Output(Bool())
  val AlmostFull = Output(Bool())
}

class LFifoReaderBundle extends Bundle {
  val Q = Output(UInt(8.W)) 
  val RdClock = Input(Clock())
  val RdEn = Input(Bool())
  val Empty = Output(Bool())
  val AlmostEmpty = Output(Bool())
}

class LatticeFIFOWrapper extends RawModule {
  val io = IO(new Bundle{
    val w = new LFifoWriterBundle
    val r = new LFifoReaderBundle

    val reset = Input(Bool())
  })
  val fifo = Module(new LatticeFIFO)
  fifo.io.Reset := io.reset

  fifo.io.Data := io.w.Data
  fifo.io.WrClock := io.w.WrClock
  fifo.io.WrEn := io.w.WrEn
  io.w.AlmostFull := fifo.io.AlmostFull
  io.w.Full := fifo.io.Full

  io.r.Q := fifo.io.Q
  fifo.io.RdClock := io.r.RdClock
  fifo.io.RdEn := io.r.RdEn
  io.r.Empty := fifo.io.Empty
  io.r.AlmostEmpty := fifo.io.AlmostEmpty
}


class LatticeFIFO extends BlackBox {
  val io = IO(new Bundle{
    val Data = Input(UInt(8.W))
    val WrClock = Input(Clock())
    val RdClock = Input(Clock())
    val WrEn = Input(Bool())
    val RdEn = Input(Bool())
    val Reset = Input(Bool())
    val RPReset = Input(Bool())

    val Q = Output(UInt(8.W))
    val Empty = Output(Bool())
    val Full = Output(Bool())
    val AlmostEmpty = Output(Bool())
    val AlmostFull = Output(Bool())
  })
}
