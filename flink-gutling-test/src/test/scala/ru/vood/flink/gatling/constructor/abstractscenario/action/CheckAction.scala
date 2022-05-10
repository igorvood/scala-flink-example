package ru.vood.flink.gatling.constructor.abstractscenario.action

import io.gatling.commons.stats.{KO, OK}
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.action.{Action, ChainableAction}
import io.gatling.core.session.Session
import io.gatling.core.structure.ScenarioContext

class CheckAction (nameAction: String, checker: Checker) extends ActionBuilder {
  override def build(ctx: ScenarioContext, nextAction: Action): Action =
    new CheckChainableAction(nameAction, nextAction, ctx, checker)
}

