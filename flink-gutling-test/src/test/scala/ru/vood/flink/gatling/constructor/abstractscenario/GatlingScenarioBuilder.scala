package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.Predef._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import ru.vood.flink.avro.AvroUtil
import ru.vood.flink.gatling.common.FooCounter
import ru.vood.flink.gatling.config.GenerationParameters

trait GatlingScenarioBuilder[DTO] extends SessionParamNames with DtoGenerate[DTO] with GatlingScenarioSender {

  val scenarioName: String

  def START_USERS: Long

  protected lazy val fooCounter = new FooCounter(START_USERS)

  implicit val generationParameters: GenerationParameters

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

  implicit val genFunction: String => DTO

  protected def dtoGenerate(session: Session)(implicit genFunction: String => DTO): Session = {
    val customer_id = session(customerIdSessionName).as[String]
    val t = genFunction(customer_id)
    //    val universalDto = UniversalDto(customer_id, Map(), Map(), Map())

    val bytesUniversalDto = AvroUtil.encode[DTO](t, encoder, writer)
    session
      .set(bytesInputDtoSessionName, bytesUniversalDto)
  }

}

