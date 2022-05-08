package ru.vood.flink.gatling.constructor.impl

import io.gatling.core.Predef.atOnceUsers
import io.gatling.core.controller.inject.open.OpenInjectionStep
import ru.vood.flink.gatling.config.GenerationParameters
import ru.vood.flink.gatling.constructor.abstractscenario.GatlingOpenInjectionStep

case class UserByTransactionStep(private val generationParam: GenerationParameters) extends GatlingOpenInjectionStep {
  override def createOpenInjectionStep: OpenInjectionStep = atOnceUsers(generationParam.countUsers)
}
