package ru.vood.flink.gatling.constructor.impl

import com.sksamuel.avro4s.{AvroSchema, Encoder}
import org.apache.avro.Schema
import ru.vood.flink.dto.UniversalDto

object DtoGenerateImplicitVal {

  implicit val schemaUniversalDto: Schema = AvroSchema[UniversalDto]

  implicit val encoderUniversalDto: Encoder[UniversalDto] = com.sksamuel.avro4s.Encoder[UniversalDto]

}
