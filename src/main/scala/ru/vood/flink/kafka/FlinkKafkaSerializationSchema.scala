package ru.vood.flink.kafka

import com.sksamuel.avro4s.AvroOutputStream
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import ru.vood.flink.dto.UniversalDto

import java.io.ByteArrayOutputStream
import java.lang
import scala.collection.JavaConverters.asJavaIterableConverter

class FlinkKafkaSerializationSchema(topic: String) extends KafkaSerializationSchema[UniversalDto] {

  override def serialize(element: UniversalDto, timestamp: lang.Long): ProducerRecord[Array[Byte], Array[Byte]] = {
    val headers = List[Header](new RecordHeader("schema", element.getClass.getSimpleName.getBytes))
    val (key, value) = serialize(element)
    new ProducerRecord(topic, null, key, value, headers.asJava)
  }

  private def serialize(data: UniversalDto): (Array[Byte], Array[Byte]) = {
    var baos: ByteArrayOutputStream = null
    var output: AvroOutputStream[UniversalDto] = null
    try {
      baos = new ByteArrayOutputStream()
      val avroOutputStream = AvroOutputStream.binary[UniversalDto]
      output = avroOutputStream.to(baos).build()
      output.write(data)
    } finally {
      if (output != null) {
        output.close()
      }

      if (baos != null) {
        baos.close()
      }
    }

    (data.uuid.getBytes(), baos.toByteArray)
  }

}