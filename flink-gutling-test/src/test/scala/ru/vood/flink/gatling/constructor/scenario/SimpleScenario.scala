package ru.vood.flink.gatling.constructor.scenario

import ru.vood.flink.gatling.config.FlinkGatlingConfig
import ru.vood.flink.gatling.constructor.abstractscenario.{GatlingOpenInjectionStep, GatlingPopulation, GatlingProtocol, GatlingScenarioBuilder}
import ru.vood.flink.gatling.constructor.impl.{KafkaProtocolCreator, OnlySendKafkaScenario, UserByTransactionStep}

case class SimpleScenario(implicit config: FlinkGatlingConfig) extends GatlingPopulation {

  override val gatlingOpenInjectionStep: GatlingOpenInjectionStep = UserByTransactionStep(config.generationParam)
  override val gatlingScenarioBuilder: GatlingScenarioBuilder = OnlySendKafkaScenario()

  override val gatlingProtocol: GatlingProtocol = KafkaProtocolCreator(config.additionalProducerGatlingProp)(config.flinkJobServiceConfiguration.kafkaConsumerProperty)

}
