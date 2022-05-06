package ru.vood.flink.kafka.consumer

import org.apache.flink.api.common.serialization.{AbstractDeserializationSchema, DeserializationSchema}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer, KafkaSerializationSchema}
import ru.vood.flink.configuration.example.{KafkaConsumerProperty, KafkaProducerProperty}

import java.util.Properties

object KafkaFactory {

  type TopicName = String

  val flinkKafkaConsumerCommitOffsetsOnCheckpoints = true


 /* implicit def des[T](implicit convert: Array[Byte] => T): DeserializationSchema[T] = {
    val typeInfo: TypeInformation[T] = createTypeInformation[T]
    new AbstractDeserializationSchema[T](typeInfo) {
      override def deserialize(message: Array[Byte]): T = convert.apply(message)
    }
  }*/

  def createKafkaConsumer[T](cp: KafkaConsumerProperty)(implicit des: DeserializationSchema[T]): FlinkKafkaConsumer[T] = {
    val consumer = new FlinkKafkaConsumer[T](cp.topicName, des, cp.propertiesConsumer.clone.asInstanceOf[Properties])
    consumer.setStartFromGroupOffsets()
    consumer.setCommitOffsetsOnCheckpoints(flinkKafkaConsumerCommitOffsetsOnCheckpoints)
    consumer
  }

  def createKafkaProducer[T](kafkaProducerProperty: KafkaProducerProperty,
                             serializer: TopicName => KafkaSerializationSchema[T],
                            ): FlinkKafkaProducer[T] = {

    new FlinkKafkaProducer[T](
      kafkaProducerProperty.topicName,
      serializer(kafkaProducerProperty.topicName),
      kafkaProducerProperty.propertiesProducers,
      kafkaProducerProperty.producerSemantic
    )
  }


}
