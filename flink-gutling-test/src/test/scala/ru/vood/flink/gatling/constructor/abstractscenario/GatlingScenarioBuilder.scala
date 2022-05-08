package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.Predef.Session
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioBuilder
import ru.vood.flink.gatling.common.FooCounter

trait GatlingScenarioBuilder extends SessionParamNames{

  def START_USERS : Long

  protected lazy val fooCounter = new FooCounter(START_USERS)

  implicit val sendToActionBuilder: ActionBuilder

  def createScenarioBuilder: ScenarioBuilder

}
