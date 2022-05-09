package ru.vood.flink.gatling.constructor.data.intf

import com.sksamuel.avro4s.{Decoder, Encoder}
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumWriter, GenericRecord}
import ru.vood.flink.gatling.constructor.data.TestCaseData

trait InputData[D] {
  def schema: Schema

  def encoder: Encoder[D]

  def decoder: Decoder[D]

  def writer: GenericDatumWriter[GenericRecord] = new GenericDatumWriter[GenericRecord](schema)

  def generate: String => TestCaseData[D]
}

trait OutData[OUT] {

}
