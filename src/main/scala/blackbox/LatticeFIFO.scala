package blackbox

import chisel3._
import chisel3.util._

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
