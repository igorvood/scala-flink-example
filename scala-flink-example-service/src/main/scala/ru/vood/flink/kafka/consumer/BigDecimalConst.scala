package ru.vood.flink.kafka.consumer

import com.sksamuel.avro4s.ScalePrecision

object BigDecimalConst {
  val SCALE: Int = 5
  val PRECISION: Int = 23

  implicit val sp: ScalePrecision = ScalePrecision(SCALE, PRECISION)
}
