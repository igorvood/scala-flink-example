package ru.vood.flink.avro

import com.sksamuel.avro4s.{Decoder, Encoder}
import org.apache.avro.generic.{GenericDatumReader, GenericDatumWriter, GenericRecord}
import org.apache.avro.io.{DecoderFactory, EncoderFactory}
import org.apache.avro.util.ByteBufferInputStream
import org.slf4j.{Logger, LoggerFactory}
import ru.vood.flink.dto.UniversalDto

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.Collections

object AvroUtil {

  private val logger: Logger = LoggerFactory.getLogger(getClass.getName)

  def encode[T](value: T, encoder: Encoder[T], writer: GenericDatumWriter[GenericRecord]): Array[Byte] = {
    val outputStream: ByteArrayOutputStream = null
    try {
      val outputStream = new ByteArrayOutputStream(512)
      val record = encoder.encode(value).asInstanceOf[GenericRecord]
      val enc = EncoderFactory.get().directBinaryEncoder(outputStream, null)
      writer.write(record, enc)
      val bytes = outputStream.toByteArray

      bytes
    } finally {
      if (outputStream != null) outputStream.close()
    }
  }

  def decode[T](bytes: ByteBuffer, decoder: Decoder[T], reader: GenericDatumReader[GenericRecord]): T = {
    var byteBuffer: ByteBufferInputStream = null
    try {
      byteBuffer = new ByteBufferInputStream(Collections.singletonList(bytes.duplicate))
      val dec = DecoderFactory.get().binaryDecoder(byteBuffer, null)
      val record = reader.read(null, dec)
      decoder.decode(record)
    }
    finally {
      if (byteBuffer != null) byteBuffer.close()
    }
  }



  @inline implicit def bytesToUniversalDto: Array[Byte] => UniversalDto = { bytes =>
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
