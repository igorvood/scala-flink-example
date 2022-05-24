package ru.vood.flink.job

import org.apache.flink.api.common.serialization.{AbstractDeserializationSchema, DeserializationSchema}
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import ru.vood.flink.avro.AvroUtil._
import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.example.{KafkaCnsProperty, KafkaConsumerProperty, KafkaPrdProperty, KafkaProducerPropertyMap}
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.kafka.FlinkKafkaSerializationSchema
import ru.vood.flink.kafka.consumer.KafkaFactory.{createKafkaConsumer, createKafkaProducer}

case class FlinkJobConfiguration(flinkConfiguration: FlinkConfiguration,
                                 kafkaMainConsumerProperty: KafkaConsumerProperty,
                                 kafkaProducerPropertyMap: KafkaProducerPropertyMap,
                                ) {

  implicit def des(implicit convert: Array[Byte] => UniversalDto): DeserializationSchema[UniversalDto] =
    new AbstractDeserializationSchema[UniversalDto]() {
      override def deserialize(message: Array[Byte]): UniversalDto = convert.apply(message)
    }

  lazy val kafkaConsumer: FlinkKafkaConsumer[UniversalDto] = createKafkaConsumer[UniversalDto](kafkaMainConsumerProperty)
  lazy val kafkaProducerMap: Map[String, FlinkKafkaProducer[UniversalDto]] = kafkaProducerPropertyMap
    .producers
    .map { prop => prop._1 -> createKafkaProducer(prop._2, { topicName => new FlinkKafkaSerializationSchema(topicName) })
    }

}


object FlinkJobConfiguration {
  val mainConsumerPrefix: String = "app.kafka.consumer."
  val filterConsumerPrefix: String = "app.kafka.filter.consumer."
  val producersPrefix: String = "app.kafka.producers."
  val producerPropPrefix: String = "app.kafka.producer."
  val kafkaPropPrefix = "property."
  val flinkConfigurationPropPrefix = "app.flink.configuration."

  def apply(implicit properties: AllApplicationProperties): FlinkJobConfiguration = {

    val consumerProperty = KafkaCnsProperty(s"$mainConsumerPrefix$kafkaPropPrefix")
    val producerProperty = KafkaPrdProperty(s"$producerPropPrefix$kafkaPropPrefix")

    new FlinkJobConfiguration(
      flinkConfiguration = FlinkConfiguration(flinkConfigurationPropPrefix),
      kafkaMainConsumerProperty = KafkaConsumerProperty.apply(mainConsumerPrefix, consumerProperty),
      kafkaProducerPropertyMap = KafkaProducerPropertyMap(producersPrefix, producerProperty)
    )
  }
}