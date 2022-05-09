package ru.vood.flink.gatling.constructor.abstractscenario.kafka

import com.sksamuel.avro4s.Decoder
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.kafka.clients.consumer.ConsumerRecord
import ru.vood.flink.avro.AvroUtil

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

trait ConsumerRecordAccumulator[V] {

  def prefix: String

  val decoder : Decoder[V]

  val records: ConcurrentHashMap[String, V] =
    new ConcurrentHashMap[String, V]()

  def addRecord(consumerRecord: ConsumerRecord[Array[Byte], Array[Byte]]): Boolean = {

    val key = new java.lang.String(consumerRecord.key(), StandardCharsets.UTF_8)

    val reader = new GenericDatumReader[GenericRecord](decoder.schema)
    val decodeAvro = AvroUtil.decode(ByteBuffer.wrap(consumerRecord.value()), decoder, reader)

    if (key.startsWith(prefix)) {
      records.put(key, decodeAvro)
      true
    } else false
  }

  def getCount: Long = records.size()

  def get(key: String): V = records.get(key)
  //  def getAll(key: K): Seq[V] =  records.get(key)
}
