package ru.vood.flink.gatling.constructor.data

import com.sksamuel.avro4s.{AvroSchema, Decoder, Encoder}
import org.apache.avro.Schema
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.gatling.constructor.data.intf.InputData

class UniversalDtoGenerator extends InputData[UniversalDto] {
  override def schema: Schema = AvroSchema[UniversalDto]

  override def encoder: Encoder[UniversalDto] = Encoder[UniversalDto]

  override def decoder: Decoder[UniversalDto] = Decoder[UniversalDto]

  override def generate: String => TestCaseData[UniversalDto] = { id =>
    val caseName = (id.hashCode % 7).toString
    TestCaseData(caseName = "CASE" + caseName, data = UniversalDto(id, Map(), Map(), Map()))
  }
}
