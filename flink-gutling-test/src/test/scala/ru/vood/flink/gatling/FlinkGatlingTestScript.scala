package ru.vood.flink.gatling

import com.github.mnogu.gatling.kafka.Predef.kafka
import com.github.mnogu.gatling.kafka.protocol.KafkaProtocol
import com.sksamuel.avro4s.{AvroSchema, Encoder}
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.session.Session
import io.gatling.core.structure.ScenarioBuilder
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumWriter, GenericRecord}
import ru.vood.flink.avro.AvroUtil
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.gatling.common.FooCounter
import ru.vood.flink.gatling.config.{FlinkGatlingConfig, GenerationParameters}
import ru.vood.flink.gatling.extension.GatlingExtension._

import java.util.Calendar
import scala.collection.mutable
import scala.util.Random

class FlinkGatlingTestScript extends Simulation {

  val config: FlinkGatlingConfig = FlinkGatlingConfig.apply()

  def startUserNumberFrom(): Long = Random.nextInt(100) * Random.nextInt(100) * 10000

  val START_USERS: Long = startUserNumberFrom()
  private val generationParam: GenerationParameters = config.generationParam
  val prefix: String = generationParam.prefixIdentity.getOrElse("")
  val fooCounter = new FooCounter(START_USERS)
  val feeder: Iterator[Map[String, Any]] = Iterator.continually(generateMsgFields)
  var expectedResultsMap = mutable.Map[String, UniversalDto]();

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


  /*


    def generateMsgFields(): Map[String, Any] = {
      val time = Calendar.getInstance().getTime.getTime

      Map(
        "uuid" -> java.util.UUID.randomUUID().toString,
        "process_timestamp" -> time,
        "expectedResultsMap" -> expectedResultsMap,
        "operation_id" -> null
      )
    }





    def startUserNumberFrom(): Long = Random.nextInt(100) * Random.nextInt(100) * 10000

    val START_USERS: Long = startUserNumberFrom()
    val fooCounter = new FooCounter(START_USERS)

  */
  private val kafkaConsumerPropertyFromService = config.flinkJobServiceConfiguration.kafkaConsumerProperty


  val kafkaConf: KafkaProtocol = kafka
    .topic(kafkaConsumerPropertyFromService.topicName)
    .properties(kafkaConsumerPropertyFromService.propertiesConsumer)

  private val customerIdSessionName = "customer_id"
  private val bytesInputDtoSessionName = "bytes_uaspDto"
  val scenarioBusinessRules = scenario(" BusinessRulesTest")
    .exec(session => {
      val customer_id = prefix + fooCounter.inc()
      val updateSession: Session = session
        .set(customerIdSessionName, customer_id)
        .set("countMessages", 0L)
      updateSession
    })
    .repeat(generationParam.countTransaction)({

      feed(feeder)
        .exec(session => {
          val customer_id = session(customerIdSessionName).as[String]
          val uaspDto = UniversalDto(Map(), Map(), Map(), "sad")

          val bytes_uaspDto = AvroUtil.encode[UniversalDto](uaspDto, encoder, writer)
          session
            .set(bytesInputDtoSessionName, bytes_uaspDto)
        })
        .exec(kafka("Request for classification").send[String, Array[Byte]]("${" + customerIdSessionName + "}", "${" + bytesInputDtoSessionName + "}")
        )
    })


  setUp(
    scenarioBusinessRules.inject(atOnceUsers(generationParam.countUsers)).protocols(kafkaConf)
  )
}
