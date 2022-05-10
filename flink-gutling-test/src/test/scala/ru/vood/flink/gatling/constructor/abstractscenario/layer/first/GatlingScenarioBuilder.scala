package ru.vood.flink.gatling.constructor.abstractscenario.layer.first

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import ru.vood.flink.avro.AvroUtil
import ru.vood.flink.gatling.common.FooCounter
import ru.vood.flink.gatling.config.GenerationParameters
import ru.vood.flink.gatling.constructor.abstractscenario.SessionParamNames
import ru.vood.flink.gatling.constructor.data.TestCaseData
import ru.vood.flink.gatling.constructor.data.intf.TestingDataType

trait GatlingScenarioBuilder[DTO_IN] extends SessionParamNames with GatlingScenarioSender {

  val testingDataType: TestingDataType[DTO_IN, DTO_IN]

  private implicit def genFunction: String => TestCaseData[DTO_IN] = testingDataType.inputMeta.genFunction

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

  protected def dtoGenerate(session: Session)(implicit genFunction: String => TestCaseData[DTO_IN]): Session = {
    val customer_id = session(customerIdSessionName).as[String]
    val testCaseData = genFunction(customer_id)
    val dto = testCaseData.data
    //    val universalDto = UniversalDto(customer_id, Map(), Map(), Map())

    val bytesUniversalDto = AvroUtil.encode[DTO_IN](dto, testingDataType.inputMeta.encoder, testingDataType.inputMeta.writer)

    session
      .set(bytesInputDtoSessionName, bytesUniversalDto)
      .set(testCaseName, testCaseData.caseName)
  }

}
