package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.action.builder.ActionBuilder

trait GatlingScenarioSender {
  implicit val sendToActionBuilder: ActionBuilder
}
