package ru.vood.flink.kafka.consumer

import org.apache.flink.api.common.serialization.{AbstractDeserializationSchema, DeserializationSchema}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import ru.vood.flink.configuration.example.KafkaConsumerProperty

import java.util.Properties

object ConsumerFactory {

  implicit def des[T](implicit convert: Array[Byte] => T): DeserializationSchema[T] =
    new AbstractDeserializationSchema[T]() {
      override def deserialize(message: Array[Byte]): T = convert.apply(message)
    }

  def getKafkaConsumer[T](cp: KafkaConsumerProperty)(implicit des: DeserializationSchema[T]): FlinkKafkaConsumer[T] = {
    val consumer = new FlinkKafkaConsumer[T](cp.topicName, des, cp.propertiesConsumer.clone.asInstanceOf[Properties])
    consumer.setStartFromGroupOffsets()
    consumer
  }

}
