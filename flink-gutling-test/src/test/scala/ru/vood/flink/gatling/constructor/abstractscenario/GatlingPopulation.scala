package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.Predef.openInjectionProfileFactory
import io.gatling.core.structure.PopulationBuilder

trait GatlingPopulation extends GatlingOpenInjectionStep with GatlingScenarioBuilder with GatlingProtocol {

  def createPopulationBuilder: PopulationBuilder =
    createScenarioBuilder.inject(createOpenInjectionStep).protocols(createProtocol)

}
