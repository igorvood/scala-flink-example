package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.Predef.openInjectionProfileFactory
import io.gatling.core.structure.PopulationBuilder

trait GatlingPopulation {

  val gatlingOpenInjectionStep: GatlingOpenInjectionStep
  val gatlingScenarioBuilder: GatlingScenarioBuilder
  val gatlingProtocol: GatlingProtocol

  def createPopulationBuilder: PopulationBuilder =
    gatlingScenarioBuilder.createScenarioBuilder.inject(gatlingOpenInjectionStep.createOpenInjectionStep).protocols(gatlingProtocol.createProtocol)

}
