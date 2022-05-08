package ru.vood.flink.gatling.constructor.impl

import com.github.mnogu.gatling.kafka.Predef.kafka
import com.sksamuel.avro4s.{AvroSchema, Encoder}
import io.gatling.core.Predef._
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioBuilder
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumWriter, GenericRecord}
import ru.vood.flink.avro.AvroUtil
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.gatling.config.{FlinkGatlingConfig, GenerationParameters}
import ru.vood.flink.gatling.constructor.abstractscenario.GatlingScenarioBuilder

import java.util.Calendar
import scala.collection.mutable
import scala.util.Random

case class OnlySendKafkaScenario(scenarioName: String) extends GatlingScenarioBuilder {

  lazy val config: FlinkGatlingConfig = FlinkGatlingConfig.apply()

  implicit lazy val generationParam: GenerationParameters = config.generationParam

  override implicit val sendToActionBuilder: ActionBuilder = kafka(scenarioName + " kafka request").send[String, Array[Byte]]("${" + customerIdSessionName + "}", "${" + bytesInputDtoSessionName + "}")

  override def START_USERS: Long = Random.nextInt(100) * Random.nextInt(100) * 10000

  lazy val feeder: Iterator[Map[String, Any]] = Iterator.continually(generateMsgFields)
  lazy val expectedResultsMap = mutable.Map[String, UniversalDto]();

  def generateMsgFields(): Map[String, Any] = {
    val time = Calendar.getInstance().getTime.getTime

    Map(
      "uuid" -> java.util.UUID.randomUUID().toString,
      "process_timestamp" -> time,
      "expectedResultsMap" -> expectedResultsMap,
      "operation_id" -> null
    )
  }

  val schema: Schema = AvroSchema[UniversalDto]
  val encoder: Encoder[UniversalDto] = com.sksamuel.avro4s.Encoder[UniversalDto]
  val writer = new GenericDatumWriter[GenericRecord](schema)

  override def createScenarioBuilder: ScenarioBuilder = {
    scenario(s"$scenarioName scenario test")
      .exec(idGenerateActionBuilder(_))
      .repeat(generationParam.countTransaction)({
        feed(feeder)
          .exec(session => {
            val customer_id = session(customerIdSessionName).as[String]
            val uaspDto = UniversalDto(Map(), Map(), Map(), "sad")

            val bytes_uaspDto = AvroUtil.encode[UniversalDto](uaspDto, encoder, writer)
            session

              .set(bytesInputDtoSessionName, bytes_uaspDto)
          })
          .exec(sendToActionBuilder)
      })
  }
}
