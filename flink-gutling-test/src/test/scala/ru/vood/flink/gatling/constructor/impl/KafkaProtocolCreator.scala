package ru.vood.flink.gatling.constructor.impl

import ru.vood.flink.configuration.example.KafkaConsumerProperty
import ru.vood.flink.gatling.config.AdditionalProducerGatlingProp
import ru.vood.flink.gatling.constructor.abstractscenario.kafka.GatlingKafkaProtocol

case class KafkaProtocolCreator(additional: AdditionalProducerGatlingProp)(implicit cnsProperty: KafkaConsumerProperty) extends GatlingKafkaProtocol {

  override implicit val additionalProducerGatlingProp: AdditionalProducerGatlingProp = additional
  override implicit val kafkaCnsProperty: KafkaConsumerProperty = cnsProperty

}


