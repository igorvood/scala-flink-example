package ru.vood.flink.gatling.constructor.abstractscenario.action

import io.gatling.commons.stats.{KO, OK}
import io.gatling.core.action.{Action, ChainableAction}
import io.gatling.core.session.Session
import io.gatling.core.structure.ScenarioContext

class CheckChainableAction(nameAction: String, nextAction: Action, ctx: ScenarioContext, checker: Checker)
  extends ChainableAction
{
  var startTime = System.currentTimeMillis()
  var stopTime = System.currentTimeMillis()

  override def next: Action = nextAction

  override def name: String = nameAction

  override def execute(session: Session): Unit = {
    try {
      val checkData: CheckData = checker.check(session)

      ctx.coreComponents.statsEngine.logResponse(
        session.scenario,
        session.groups,
        requestName = name,
        startTimestamp = startTime,
        endTimestamp = stopTime,
        status = if (checkData.checkStatus) OK else KO,
        None,
        message = if (checkData.checkStatus) None else Option(checkData.checkMsg),
      )

      nextAction ! session
    } catch {
      case e: Exception => {
        val stopTime = System.currentTimeMillis()

        ctx.coreComponents.statsEngine.logResponse(
          session.scenario,
          session.groups,
          requestName = name,
          startTimestamp = startTime,
          endTimestamp = stopTime,
          status = KO,
          None,
          Some(e.getMessage)
        )

        nextAction ! session
      }
    }

  }
}
