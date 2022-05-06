package ru.vood.flink.job

import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import ru.vood.flink.avro.AvroUtil._
import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.example.{KafkaCnsProperty, KafkaConsumerProperty, KafkaPrdProperty, KafkaProducerPropertyMap}
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.kafka.FlinkKafkaSerializationSchema
import ru.vood.flink.kafka.consumer.KafkaFactory.{createKafkaConsumer, createKafkaProducer, des}

case class FlinkJobConfiguration(kafkaConsumerProperty: KafkaConsumerProperty,
                                 kafkaProducerPropertyMap: KafkaProducerPropertyMap
                                ) {

  lazy val kafkaConsumer: FlinkKafkaConsumer[UniversalDto] = createKafkaConsumer[UniversalDto](kafkaConsumerProperty)
  lazy val kafkaProducerMap: Map[String, FlinkKafkaProducer[UniversalDto]] = kafkaProducerPropertyMap
    .producers
    .map { prop => prop._1 -> createKafkaProducer(prop._2, { topicName => new FlinkKafkaSerializationSchema(topicName) })
    }

}


object FlinkJobConfiguration {
  val consumerPrefix: String = "app.kafka.consumer."
  val producerPrefix: String = "app.kafka.producers."
  val kafkaPropPrefix = "property."

  def apply(implicit properties: AllApplicationProperties): FlinkJobConfiguration = {

    val consumerProperty = KafkaCnsProperty(s"$consumerPrefix$kafkaPropPrefix")
    val producerProperty = KafkaPrdProperty(s"$producerPrefix$kafkaPropPrefix")

    new FlinkJobConfiguration(
      kafkaConsumerProperty = KafkaConsumerProperty.apply(consumerPrefix, consumerProperty),
      kafkaProducerPropertyMap = KafkaProducerPropertyMap(producerPrefix, producerProperty)
    )
  }
}