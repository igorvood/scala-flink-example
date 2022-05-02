package ru.vood.flink.job

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import ru.vood.flink.configuration.example.KafkaConsumerProperty
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.kafka.consumer.ConsumerFactory.{des, getKafkaConsumer}
import ru.vood.flink.avro.AvroUtil._

case class FlinkJobConfiguration(kafkaConsumerProperty: KafkaConsumerProperty
                                ) {

  lazy val kafkaConsumer: FlinkKafkaConsumer[UniversalDto] = getKafkaConsumer[UniversalDto](kafkaConsumerProperty)
}
