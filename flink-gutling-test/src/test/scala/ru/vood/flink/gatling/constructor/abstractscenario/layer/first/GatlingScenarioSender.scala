package ru.vood.flink.gatling.constructor.abstractscenario.layer.first

import io.gatling.core.action.builder.ActionBuilder

trait GatlingScenarioSender {
  implicit val sendToActionBuilder: ActionBuilder
}
