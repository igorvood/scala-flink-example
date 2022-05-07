package ru.vood.flink.gatling.extension

import com.github.mnogu.gatling.kafka.protocol.KafkaProtocol
import org.apache.kafka.clients.producer.ProducerConfig

import java.util.Properties
import scala.collection.mutable

object GatlingExtension {

  implicit final class GatlingKafkaProtocolPreDef(self: KafkaProtocol) {

    def properties(prop: Properties): KafkaProtocol = {
      val bufferProp: mutable.Map[String, Object] = mutable.HashMap[String, Object]()
      prop.stringPropertyNames()
        .stream()
        .forEach(q => bufferProp.put(q, prop.getProperty(q)))

      bufferProp.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer")
      bufferProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer")

      val mapPropForKafka = bufferProp.toMap
      self.properties(mapPropForKafka)
    }
  }

}
