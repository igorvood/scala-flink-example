package ru.vood.flink.configuration.example

import ru.vood.flink.configuration.PropertyUtil.mapProperty
import ru.vood.flink.configuration.{AllApplicationProperties, PrefixProperty}

case class KafkaProducerPropertyMap(producers: Map[String, KafkaProducerProperty]){

  require(producers.nonEmpty, "producers must contains at last one producer")
}


object KafkaProducerPropertyMap {

  def apply(prefix: String,
            kafkaProperty: KafkaProperty)(
             implicit appProps: AllApplicationProperties
           ): KafkaProducerPropertyMap = {
    PrefixProperty(prefix)
      .createPropertyData { prf =>
        KafkaProducerPropertyMap(
          producers = mapProperty(prf, { (str, appProps) => KafkaProducerProperty(str, kafkaProperty)(appProps) })
        )
      }
  }

}
