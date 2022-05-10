package ru.vood.flink.gatling.constructor.abstractscenario.layer.first

import io.gatling.core.controller.inject.open.OpenInjectionStep

trait GatlingOpenInjectionStep {

  def createOpenInjectionStep: OpenInjectionStep

}
