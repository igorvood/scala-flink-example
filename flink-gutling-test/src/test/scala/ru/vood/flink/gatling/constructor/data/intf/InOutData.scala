package ru.vood.flink.gatling.constructor.data.intf

import com.sksamuel.avro4s.{Decoder, Encoder}
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumWriter, GenericRecord}
import ru.vood.flink.gatling.constructor.data.TestCaseData

trait InputData[SELF, OUT] {

  type Out = OutData[OUT, SELF]

  def transform(v: SELF): OUT

  def schema: Schema // = AvroSchema[UniversalDto]

  def encoder: Encoder[SELF] //= Encoder[UniversalDto]

  def decoder: Decoder[SELF] // = Decoder[UniversalDto]

  val writer = new GenericDatumWriter[GenericRecord](schema)

  /*Эта функция не должна содержать никакх рандомов, должна работать как чистая ф-ция, при одном и том же
  * значении на входе всегда одно и тоже зн на выходе*/
  implicit def genFunction: String => TestCaseData[SELF]
}

trait OutData[SELF, IN] {

  type In = InputData[IN, SELF]

  def schema: Schema // = AvroSchema[UniversalDto]

  def encoder: Encoder[SELF] //= Encoder[UniversalDto]

  def decoder: Decoder[SELF] // = Decoder[UniversalDto]

  val writer = new GenericDatumWriter[GenericRecord](schema)

}

trait TestingDataType[IN, OUT] {

  val inputMeta: InputData[IN, OUT]

  val outputMeta: OutData[OUT, IN]

}
