package ru.vood.flink.gatling.constructor.abstractscenario.layer.first

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import ru.vood.flink.avro.AvroUtil
import ru.vood.flink.gatling.common.FooCounter
import ru.vood.flink.gatling.config.GenerationParameters
import ru.vood.flink.gatling.constructor.abstractscenario.SessionParamNamesConvensions
import ru.vood.flink.gatling.constructor.data.TestCaseData
import ru.vood.flink.gatling.constructor.data.intf.TestingDataType

trait GatlingScenarioBuilder[DTO_IN] extends SessionParamNamesConvensions with GatlingScenarioSender {

  val testingDataType: TestingDataType[DTO_IN, DTO_IN]

  private implicit def genFunction: String => TestCaseData[DTO_IN] = testingDataType.inputMeta.genFunction

  val scenarioName: String

  implicit def generationParameters: GenerationParameters

  protected lazy val fooCounter = new FooCounter(generationParameters.startUsersIdentity)



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

  protected def idGenerateActionBuilder(session: Session)(implicit generationParameters: GenerationParameters): Session = {
    val prefix = generationParameters.prefixIdentity.getOrElse("")
    val customer_id = prefix + fooCounter.inc()
    val updateSession: Session = session
      .set(inputIdDtoSessionName, customer_id)
      .set(countMessages, 0L)
    updateSession
  }

  protected def dtoGenerate(session: Session)(implicit genFunction: String => TestCaseData[DTO_IN]): Session = {
    val customer_id = session(inputIdDtoSessionName).as[String]
    val testCaseData = genFunction(customer_id)
    val bytesUniversalDto = AvroUtil.encode[DTO_IN](testCaseData.data, testingDataType.inputMeta.encoder, testingDataType.inputMeta.writer)
    session
      .set(bytesInputDtoSessionName, bytesUniversalDto)
      .set(testCaseName, testCaseData.caseName)
  }

}
