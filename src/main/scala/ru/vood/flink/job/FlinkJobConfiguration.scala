package ru.vood.flink.job

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import ru.vood.flink.avro.AvroUtil._
import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.example.{KafkaCnsProperty, KafkaConsumerProperty}
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.kafka.consumer.ConsumerFactory.{des, getKafkaConsumer}

case class FlinkJobConfiguration(kafkaConsumerProperty: KafkaConsumerProperty
                                ) {

  lazy val kafkaConsumer: FlinkKafkaConsumer[UniversalDto] = getKafkaConsumer[UniversalDto](kafkaConsumerProperty)
}


object FlinkJobConfiguration {
  val consumerPrefix = "app.kafka.consumer."

  def apply(implicit properties: AllApplicationProperties): FlinkJobConfiguration = {

    val consumerProperty = KafkaCnsProperty(consumerPrefix + "property.")

    new FlinkJobConfiguration(
      KafkaConsumerProperty.apply(consumerPrefix, consumerProperty)
    )
  }
}