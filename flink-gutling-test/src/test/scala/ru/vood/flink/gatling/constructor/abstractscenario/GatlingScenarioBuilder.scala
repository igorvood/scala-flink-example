package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.Predef.Session
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioBuilder
import ru.vood.flink.gatling.common.FooCounter
import ru.vood.flink.gatling.config.GenerationParameters

trait GatlingScenarioBuilder extends SessionParamNames {

  val scenarioName: String

  def START_USERS: Long

  protected lazy val fooCounter = new FooCounter(START_USERS)

  implicit val sendToActionBuilder: ActionBuilder

  def createScenarioBuilder: ScenarioBuilder

  def idGenerateActionBuilder(session: Session)(implicit generationParameters: GenerationParameters): Session = {
    val prefix = generationParameters.prefixIdentity.getOrElse("")
    val customer_id = prefix + fooCounter.inc()
    val updateSession: Session = session
      .set(customerIdSessionName, customer_id)
      .set(countMessages, 0L)
    updateSession
  }

}
