package ru.vood.flink.gatling.constructor.impl

import com.github.mnogu.gatling.kafka.Predef.kafka
import com.github.mnogu.gatling.kafka.protocol.KafkaProtocol
import io.gatling.core.Predef._
import io.gatling.core.protocol.Protocol
import ru.vood.flink.configuration.example.KafkaConsumerProperty
import ru.vood.flink.gatling.config.AdditionalProducerGatlingProp
import ru.vood.flink.gatling.constructor.abstractscenario.GatlingProtocol
import ru.vood.flink.gatling.constructor.impl.KafkaProtocolCreator.GatlingKafkaProtocolPreDef

import java.util.Properties
import scala.collection.mutable

class KafkaProtocolCreator(additional: AdditionalProducerGatlingProp)(implicit kafkaCnsProperty: KafkaConsumerProperty) extends GatlingProtocol {


  override val additionalProducerGatlingProp: AdditionalProducerGatlingProp = additional

  override def createProtocol(implicit additionalProducerGatlingProp: AdditionalProducerGatlingProp): Protocol = kafka
    .topic(kafkaCnsProperty.topicName)
    .propertiesFromProp(kafkaCnsProperty.propertiesConsumer)
}


object KafkaProtocolCreator {

  implicit final class GatlingKafkaProtocolPreDef(self: KafkaProtocol) {

    def propertiesFromProp(prop: Properties)(implicit additionalProducerGatlingProp: AdditionalProducerGatlingProp): KafkaProtocol = {
      val bufferProp: mutable.Map[String, Object] = mutable.HashMap[String, Object]()
      prop.stringPropertyNames()
        .stream()
        .forEach(q => bufferProp.put(q, prop.getProperty(q)))

      val mapPropForKafka = (bufferProp ++ additionalProducerGatlingProp.addPrp).toMap

      /* bufferProp.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
       //      bufferProp.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer")
       bufferProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer")

       val mapPropForKafka = bufferProp.toMap*/
      self.properties(mapPropForKafka)
    }
  }
}