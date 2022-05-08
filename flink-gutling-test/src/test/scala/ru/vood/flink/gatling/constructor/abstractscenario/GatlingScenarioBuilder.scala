package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioBuilder

trait GatlingScenarioBuilder {

  implicit val sendToActionBuilder: ActionBuilder

  def createScenarioBuilder: ScenarioBuilder

}
