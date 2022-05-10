package ru.vood.flink.gatling.constructor.abstractscenario.layer.first

import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder


trait GatlingPopulation[DTO_IN] {

  val gatlingOpenInjectionStep: GatlingOpenInjectionStep
  val gatlingScenarioBuilder: GatlingScenarioBuilder[DTO_IN]
  val gatlingProtocol: GatlingProtocol

  def createPopulationBuilder: PopulationBuilder =
    gatlingScenarioBuilder.createScenarioBuilder.inject(gatlingOpenInjectionStep.createOpenInjectionStep).protocols(gatlingProtocol.createProtocol)

}
