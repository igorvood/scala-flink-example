package ru.vood.flink.gatling.constructor.data.intf

import com.sksamuel.avro4s.{Decoder, Encoder}
import org.apache.avro.Schema

trait InputData[SELF, OUT] {

  type Out = OutData[OUT, SELF]

  def transform(v: SELF): OUT

   def schema: Schema// = AvroSchema[UniversalDto]

   def encoder: Encoder[SELF] //= Encoder[UniversalDto]

   def decoder: Decoder[SELF] // = Decoder[UniversalDto]
}

trait OutData[SELF, IN] {

  type In = InputData[IN, SELF]

  def schema: Schema// = AvroSchema[UniversalDto]

  def encoder: Encoder[SELF] //= Encoder[UniversalDto]

  def decoder: Decoder[SELF] // = Decoder[UniversalDto]

}

trait TestingDataType[IN, OUT]{

  val inputMeta : InputData[IN, OUT]

  val outputMeta : OutData[OUT, IN]

}
