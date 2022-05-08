package ru.vood.flink.gatling.constructor.impl

import com.github.mnogu.gatling.kafka.Predef.kafka
import com.sksamuel.avro4s.{AvroSchema, Encoder}
import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioBuilder
import org.apache.avro.Schema
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.gatling.config.{FlinkGatlingConfig, GenerationParameters}
import ru.vood.flink.gatling.constructor.abstractscenario.GatlingScenarioBuilder

import scala.util.Random

case class OnlySendKafkaScenario(scenarioName: String) extends GatlingScenarioBuilder[UniversalDto] {


  override def schema: Schema = AvroSchema[UniversalDto]

  override def encoder: Encoder[UniversalDto] = Encoder[UniversalDto]

  lazy val config: FlinkGatlingConfig = FlinkGatlingConfig.apply()

  implicit lazy val generationParam: GenerationParameters = config.generationParam

  override implicit val sendToActionBuilder: ActionBuilder = kafka(scenarioName + " kafka request").send[String, Array[Byte]]("${" + customerIdSessionName + "}", "${" + bytesInputDtoSessionName + "}")

  override def START_USERS: Long = Random.nextInt(100) * Random.nextInt(100) * 10000

  //  val schema: Schema = AvroSchema[UniversalDto]
  //  val encoder: Encoder[UniversalDto] = com.sksamuel.avro4s.Encoder[UniversalDto]
  //  val writer = new GenericDatumWriter[GenericRecord](schema)

  /*def dtoGenerate[T](session: Session)(implicit genFunction: String =>T): Session = {
    val customer_id = session(customerIdSessionName).as[String]
    val t = genFunction(customer_id)
//    val universalDto = UniversalDto(customer_id, Map(), Map(), Map())

    val bytesUniversalDto = AvroUtil.encode[T](t, encoder, writer)
    session
      .set(bytesInputDtoSessionName, bytesUniversalDto)


  }*/


  override implicit val genFunction: String => UniversalDto = s => UniversalDto(s, Map(), Map(), Map())

  override def createScenarioBuilder: ScenarioBuilder = {
    scenario(s"$scenarioName scenario test")
      .exec(idGenerateActionBuilder(_))
      .repeat(generationParam.countTransaction)({
        exec(dtoGenerate(_))
          .exec(sendToActionBuilder)
      })
  }
}
