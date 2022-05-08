package ru.vood.flink.gatling.constructor.scenario

import ru.vood.flink.configuration.example.KafkaConsumerProperty
import ru.vood.flink.gatling.config.{AdditionalProducerGatlingProp, FlinkGatlingConfig}
import ru.vood.flink.gatling.constructor.abstractscenario.kafka.GatlingKafkaPopulation
import ru.vood.flink.gatling.constructor.abstractscenario.{GatlingOpenInjectionStep, GatlingScenarioBuilder}
import ru.vood.flink.gatling.constructor.impl.{OnlySendKafkaScenario, UserByTransactionStep}

case class SimpleScenario(implicit config: FlinkGatlingConfig) extends GatlingKafkaPopulation {

  override def kafkaConsumerProperty: KafkaConsumerProperty = config.flinkJobServiceConfiguration.kafkaConsumerProperty

  override def additionalProducerGatlingProp: AdditionalProducerGatlingProp = config.additionalProducerGatlingProp

  override val gatlingOpenInjectionStep: GatlingOpenInjectionStep = UserByTransactionStep(config.generationParam)

  override val gatlingScenarioBuilder: GatlingScenarioBuilder = OnlySendKafkaScenario("DATA1")


}
