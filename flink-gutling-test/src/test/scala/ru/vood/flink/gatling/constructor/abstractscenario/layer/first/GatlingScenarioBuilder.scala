package ru.vood.flink.gatling.constructor.abstractscenario.layer.first

import io.gatling.core.Predef.{Session, exec, scenario}
import io.gatling.core.structure.ScenarioBuilder
import ru.vood.flink.avro.AvroUtil
import ru.vood.flink.gatling.common.FooCounter
import ru.vood.flink.gatling.config.GenerationParameters
import ru.vood.flink.gatling.constructor.abstractscenario.{DtoGenerate, GatlingScenarioSender, SessionParamNames}
import ru.vood.flink.gatling.constructor.data.TestCaseData
import io.gatling.core.Predef._

trait GatlingScenarioBuilder[DTO_IN] extends SessionParamNames with DtoGenerate[DTO_IN] with GatlingScenarioSender {

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

  implicit val genFunction: String => TestCaseData[DTO_IN]

  protected def dtoGenerate(session: Session)(implicit genFunction: String => TestCaseData[DTO_IN]): Session = {
    val customer_id = session(customerIdSessionName).as[String]
    val testCaseData = genFunction(customer_id)
    val dto = testCaseData.data
    //    val universalDto = UniversalDto(customer_id, Map(), Map(), Map())

    val bytesUniversalDto = AvroUtil.encode[DTO_IN](dto, encoder, writer)

    session
      .set(bytesInputDtoSessionName, bytesUniversalDto)
      .set(testCaseName, testCaseData.caseName)
  }

}
