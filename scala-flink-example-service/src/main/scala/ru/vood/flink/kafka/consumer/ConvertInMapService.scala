package ru.vood.flink.kafka.consumer

import org.apache.flink.api.common.functions.RichMapFunction
import org.slf4j.{Logger, LoggerFactory}
import ru.vood.flink.avro.AvroUtil.bytesToUniversalDto
import ru.vood.flink.dto.UniversalDto

class ConvertInMapService extends RichMapFunction[Array[Byte], UniversalDto] {
  private val logger: Logger = LoggerFactory.getLogger(getClass.getName)

  override def map(bytes: Array[Byte]): UniversalDto = {
    bytesToUniversalDto(bytes)
  }

}