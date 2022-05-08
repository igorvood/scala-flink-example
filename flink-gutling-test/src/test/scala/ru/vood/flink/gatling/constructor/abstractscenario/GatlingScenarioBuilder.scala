package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.structure.ScenarioBuilder


trait GatlingScenarioBuilder  {

  def createScenarioBuilder: ScenarioBuilder

}
