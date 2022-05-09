package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import ru.vood.flink.avro.AvroUtil
import ru.vood.flink.configuration.example.KafkaProducerProperty
import ru.vood.flink.gatling.common.FooCounter
import ru.vood.flink.gatling.config.GenerationParameters
import ru.vood.flink.gatling.constructor.abstractscenario.kafka.{ConsumerRecordAccumulator, ConsumerService}

trait GatlingScenarioBuilder[DTO_IN] extends SessionParamNames with DtoGenerate[DTO_IN] with GatlingScenarioSender{

  val scenarioName: String

  def startUsersNum: Long

  protected lazy val fooCounter = new FooCounter(startUsersNum)

  implicit val generationParameters: GenerationParameters

/*
  def kafkaPropertyForConsumerService: KafkaProducerProperty

  implicit val consumerRecordAccumulator: ConsumerRecordAccumulator[DTO_OUT]


  val consumerService: ConsumerService[DTO_OUT] = ConsumerService.factory(kafkaPropertyForConsumerService)
  */


  def createScenarioBuilder: ScenarioBuilder = {
    scenario(s"$scenarioName scenario test")
      .exec(idGenerateActionBuilder(_))
      .repeat(generationParameters.countTransaction)({
        exec(dtoGenerate(_))
          .exec(sendToActionBuilder)
      })

  }

  def idGenerateActionBuilder(session: Session)(implicit generationParameters: GenerationParameters): Session = {
    val prefix = generationParameters.prefixIdentity.getOrElse("")
    val customer_id = prefix + fooCounter.inc()
    val updateSession: Session = session
      .set(customerIdSessionName, customer_id)
      .set(countMessages, 0L)
    updateSession
  }

  implicit val genFunction: String => DTO_IN

  protected def dtoGenerate(session: Session)(implicit genFunction: String => DTO_IN): Session = {
    val customer_id = session(customerIdSessionName).as[String]
    val t = genFunction(customer_id)
    //    val universalDto = UniversalDto(customer_id, Map(), Map(), Map())

    val bytesUniversalDto = AvroUtil.encode[DTO_IN](t, encoder, writer)
    session
      .set(bytesInputDtoSessionName, bytesUniversalDto)
  }

}

