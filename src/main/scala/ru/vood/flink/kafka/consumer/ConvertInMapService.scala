package ru.vood.flink.kafka.consumer

import com.sksamuel.avro4s.Decoder
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.flink.api.common.functions.RichMapFunction
import org.slf4j.{Logger, LoggerFactory}
import ru.vood.flink.avro.AvroUtil
import ru.vood.flink.dto.UniversalDto

import java.nio.ByteBuffer

class ConvertInMapService extends RichMapFunction[Array[Byte], UniversalDto] {
  private val logger: Logger = LoggerFactory.getLogger(getClass.getName)

  override def map(bytes: Array[Byte]): UniversalDto = {
    try {

      val decoder = Decoder[UniversalDto]
      val reader = new GenericDatumReader[GenericRecord](decoder.schema)

      val dto = AvroUtil.decode(ByteBuffer.wrap(bytes), decoder, reader)
      dto
    } catch {
      case e: Throwable => e match {
        case _ =>
          logger.error(e.getMessage)
          throw new RuntimeException(e)
      }
    }
  }

}