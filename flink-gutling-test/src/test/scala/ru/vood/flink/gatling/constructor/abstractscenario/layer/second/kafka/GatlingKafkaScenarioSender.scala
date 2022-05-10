package ru.vood.flink.gatling.constructor.abstractscenario.layer.second.kafka

import com.github.mnogu.gatling.kafka.Predef.kafka
import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import ru.vood.flink.gatling.constructor.abstractscenario.SessionParamNames
import ru.vood.flink.gatling.constructor.abstractscenario.layer.first.GatlingScenarioSender

trait GatlingKafkaScenarioSender extends GatlingScenarioSender with SessionParamNames {

  val scenarioName: String

  override implicit val sendToActionBuilder: ActionBuilder = kafka(scenarioName + " ${" + testCaseName + "} kafka request").send[String, Array[Byte]]("${" + inputIdDtoSessionName + "}", "${" + bytesInputDtoSessionName + "}")
}
