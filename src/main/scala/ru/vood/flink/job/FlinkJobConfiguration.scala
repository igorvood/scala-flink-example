package ru.vood.flink.job

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import ru.vood.flink.avro.AvroUtil._
import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.example.{KafkaCnsProperty, KafkaConsumerProperty, KafkaPrdProperty, KafkaProducerPropertyMap}
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.kafka.consumer.ConsumerFactory.{des, getKafkaConsumer}

case class FlinkJobConfiguration(kafkaConsumerProperty: KafkaConsumerProperty,
                                 kafkaProducerPropertyMap: KafkaProducerPropertyMap
                                ) {

  lazy val kafkaConsumer: FlinkKafkaConsumer[UniversalDto] = getKafkaConsumer[UniversalDto](kafkaConsumerProperty)
}


object FlinkJobConfiguration {
  val consumerPrefix: String = "app.kafka.consumer."
  val producerPrefix: String = "app.kafka.producers."

  def apply(implicit properties: AllApplicationProperties): FlinkJobConfiguration = {

    val consumerProperty = KafkaCnsProperty(consumerPrefix + "property.")
    val producerProperty = KafkaPrdProperty(producerPrefix + "property.")

    new FlinkJobConfiguration(
      kafkaConsumerProperty = KafkaConsumerProperty.apply(consumerPrefix, consumerProperty),
      kafkaProducerPropertyMap = KafkaProducerPropertyMap(producerPrefix, producerProperty)
    )
  }
}