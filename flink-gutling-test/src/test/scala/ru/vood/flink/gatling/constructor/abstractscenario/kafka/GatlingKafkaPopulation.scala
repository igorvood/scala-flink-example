package ru.vood.flink.gatling.constructor.abstractscenario.kafka

import ru.vood.flink.configuration.example.KafkaConsumerProperty
import ru.vood.flink.gatling.config.AdditionalProducerGatlingProp
import ru.vood.flink.gatling.constructor.abstractscenario.{GatlingPopulation, GatlingProtocol}
import ru.vood.flink.gatling.constructor.impl.KafkaProtocolCreator

trait GatlingKafkaPopulation[DTO] extends GatlingPopulation[DTO] {

  def kafkaConsumerProperty: KafkaConsumerProperty

  def additionalProducerGatlingProp: AdditionalProducerGatlingProp

  override val gatlingProtocol: GatlingProtocol = KafkaProtocolCreator(additionalProducerGatlingProp, kafkaConsumerProperty)
}
