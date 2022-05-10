package ru.vood.flink.gatling.constructor.data

import com.sksamuel.avro4s.{AvroSchema, Decoder, Encoder}
import org.apache.avro.Schema
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.gatling.constructor.data.intf.{InputData, OutData, TestingDataType}

object UniversalDtoGenerator extends TestingDataType[UniversalDto, UniversalDto] {

  override val inputMeta: InputData[UniversalDto, UniversalDto] = new InputData[UniversalDto, UniversalDto] {
    override def transform(v: UniversalDto): UniversalDto = v

    override def schema: Schema = AvroSchema[UniversalDto]

    override def encoder: Encoder[UniversalDto] = Encoder[UniversalDto]

    override def decoder: Decoder[UniversalDto] = Decoder[UniversalDto]
  }

  override val outputMeta: OutData[UniversalDto, UniversalDto] = new OutData[UniversalDto, UniversalDto] {

    override def schema: Schema = AvroSchema[UniversalDto]

    override def encoder: Encoder[UniversalDto] = Encoder[UniversalDto]

    override def decoder: Decoder[UniversalDto] = Decoder[UniversalDto]
  }
}
