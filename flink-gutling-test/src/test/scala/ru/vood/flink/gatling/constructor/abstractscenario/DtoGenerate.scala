package ru.vood.flink.gatling.constructor.abstractscenario

import com.sksamuel.avro4s.Encoder
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumWriter, GenericRecord}

trait DtoGenerate[DTO] {

  /*
    val schema: Schema = AvroSchema[DTO]
    val encoder: Encoder[DTO] = com.sksamuel.avro4s.Encoder[DTO]
  */
  def schema: Schema
  def encoder: Encoder[DTO]
  def writer = new GenericDatumWriter[GenericRecord](schema)



  /* val schema: Schema = AvroSchema[UniversalDto]
   val encoder: Encoder[UniversalDto] = com.sksamuel.avro4s.Encoder[UniversalDto]
   val writer = new GenericDatumWriter[GenericRecord](schema)*/

}
