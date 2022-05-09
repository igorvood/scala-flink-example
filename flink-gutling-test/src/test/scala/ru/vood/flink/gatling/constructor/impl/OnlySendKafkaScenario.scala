package ru.vood.flink.gatling.constructor.impl

import com.sksamuel.avro4s.{AvroSchema, Encoder}
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.apache.avro.Schema
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.gatling.config.{FlinkGatlingConfig, GenerationParameters}
import ru.vood.flink.gatling.constructor.abstractscenario.GatlingScenarioBuilder
import ru.vood.flink.gatling.constructor.abstractscenario.kafka.GatlingKafkaScenarioSender

import scala.util.Random

case class OnlySendKafkaScenario(scenarioName: String) extends GatlingScenarioBuilder[UniversalDto] with GatlingKafkaScenarioSender {

  override def schema: Schema = AvroSchema[UniversalDto]

  override def encoder: Encoder[UniversalDto] = Encoder[UniversalDto]

  lazy val config: FlinkGatlingConfig = FlinkGatlingConfig.apply()

  override implicit val generationParameters: GenerationParameters = config.generationParam

  override def START_USERS: Long = Random.nextInt(100) * Random.nextInt(100) * 10000

  override implicit val genFunction: String => UniversalDto = s => UniversalDto(s, Map(), Map(), Map())

 /* override def createScenarioBuilder: ScenarioBuilder = {
    scenario(s"$scenarioName scenario test")
      .exec(idGenerateActionBuilder(_))
      .repeat(generationParameters.countTransaction)({
        exec(dtoGenerate(_))
          .exec(sendToActionBuilder)
      })
  }*/
}
