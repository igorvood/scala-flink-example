package ru.vood.flink.gatling.constructor.impl

import ru.vood.flink.configuration.example.KafkaConsumerProperty
import ru.vood.flink.gatling.config.AdditionalProducerGatlingProp
import ru.vood.flink.gatling.constructor.abstractscenario.layer.second.kafka.GatlingKafkaProtocol

case class KafkaProtocolCreator(additionalProducerGatlingProp: AdditionalProducerGatlingProp, kafkaCnsProperty: KafkaConsumerProperty) extends GatlingKafkaProtocol


