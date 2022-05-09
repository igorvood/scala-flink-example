package ru.vood.flink.gatling.constructor.impl

import com.sksamuel.avro4s.{AvroSchema, Encoder}
import org.apache.avro.Schema
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.gatling.config.{FlinkGatlingConfig, GenerationParameters}
import ru.vood.flink.gatling.constructor.abstractscenario.kafka.GatlingKafkaScenarioSender
import ru.vood.flink.gatling.constructor.abstractscenario.{GatlingScenarioBuilder, TestCaseData}

import scala.util.Random

case class OnlySendKafkaScenario(scenarioName: String) extends GatlingScenarioBuilder[UniversalDto] with GatlingKafkaScenarioSender {

  override def schema: Schema = AvroSchema[UniversalDto]

  override def encoder: Encoder[UniversalDto] = Encoder[UniversalDto]

  lazy val config: FlinkGatlingConfig = FlinkGatlingConfig()

  override implicit val generationParameters: GenerationParameters = config.generationParam

  override def startUsersNum: Long = Random.nextInt(100) * Random.nextInt(100) * 10000

  override implicit val genFunction: String => TestCaseData[UniversalDto] = { s =>
    val i = (s.hashCode % 7).toString
    TestCaseData(caseName = i, data = UniversalDto(s, Map(), Map(), Map()))
  }
}
