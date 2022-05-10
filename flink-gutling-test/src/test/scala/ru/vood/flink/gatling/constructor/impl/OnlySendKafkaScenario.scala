package ru.vood.flink.gatling.constructor.impl

import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.gatling.config.{FlinkGatlingConfig, GenerationParameters}
import ru.vood.flink.gatling.constructor.abstractscenario.layer.first.GatlingScenarioBuilder
import ru.vood.flink.gatling.constructor.abstractscenario.layer.second.kafka.GatlingKafkaScenarioSender
import ru.vood.flink.gatling.constructor.data.intf.TestingDataType
import ru.vood.flink.gatling.constructor.scenario.UniversalDtoGenerator

import scala.util.Random

case class OnlySendKafkaScenario(scenarioName: String) extends GatlingScenarioBuilder[UniversalDto] with GatlingKafkaScenarioSender {

  lazy val config: FlinkGatlingConfig = FlinkGatlingConfig()

  override val testingDataType: TestingDataType[UniversalDto, UniversalDto] = UniversalDtoGenerator

  override implicit val generationParameters: GenerationParameters = config.generationParam

  override def startUsersNum: Long = Random.nextInt(100) * Random.nextInt(100) * 10000

}
