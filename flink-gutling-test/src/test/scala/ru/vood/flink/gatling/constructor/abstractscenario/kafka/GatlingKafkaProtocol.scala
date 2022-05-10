package ru.vood.flink.gatling.constructor.abstractscenario.kafka

import com.github.mnogu.gatling.kafka.Predef.kafka
import com.github.mnogu.gatling.kafka.protocol.KafkaProtocol
import io.gatling.core.Predef._
import io.gatling.core.protocol.Protocol
import ru.vood.flink.configuration.example.KafkaConsumerProperty
import ru.vood.flink.gatling.config.AdditionalProducerGatlingProp
import ru.vood.flink.gatling.constructor.abstractscenario.kafka.GatlingKafkaProtocol.GatlingKafkaProtocolPreDef
import ru.vood.flink.gatling.constructor.abstractscenario.layer.first.GatlingProtocol

import java.util.Properties
import scala.collection.mutable

trait GatlingKafkaProtocol extends GatlingProtocol {

  implicit val additionalProducerGatlingProp: AdditionalProducerGatlingProp

  val kafkaCnsProperty: KafkaConsumerProperty

  override def createProtocol: Protocol = kafka
    .topic(kafkaCnsProperty.topicName)
    .propertiesFromProp(kafkaCnsProperty.propertiesConsumer)
}

object GatlingKafkaProtocol {

  implicit final class GatlingKafkaProtocolPreDef(self: KafkaProtocol) {

    def propertiesFromProp(prop: Properties)(implicit additionalProducerGatlingProp: AdditionalProducerGatlingProp): KafkaProtocol = {
      val bufferProp: mutable.Map[String, Object] = mutable.HashMap[String, Object]()
      prop.stringPropertyNames()
        .stream()
        .forEach(q => bufferProp.put(q, prop.getProperty(q)))
      val mapPropForKafka = (bufferProp ++ additionalProducerGatlingProp.addPrp).toMap
      self.properties(mapPropForKafka)
    }
  }
}