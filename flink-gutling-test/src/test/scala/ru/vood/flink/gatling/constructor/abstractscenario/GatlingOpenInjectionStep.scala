package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.controller.inject.open.OpenInjectionStep

trait GatlingOpenInjectionStep {

  def createOpenInjectionStep : OpenInjectionStep

}
