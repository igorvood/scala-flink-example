package ru.vood.flink.gatling.abstractscenario

import io.gatling.core.structure.ScenarioBuilder


trait GatlingScenarioBuilder  {

  def createScenarioBuilder: ScenarioBuilder

}
