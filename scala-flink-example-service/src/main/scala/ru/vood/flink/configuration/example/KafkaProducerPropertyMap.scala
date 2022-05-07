package ru.vood.flink.configuration.example

import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.PrefixProperty.PredefPrefix
import ru.vood.flink.configuration.PropertyUtil.mapProperty

case class KafkaProducerPropertyMap(producers: Map[String, KafkaProducerProperty]) {

  require(producers.nonEmpty, "producers must contains at last one producer")
}

object KafkaProducerPropertyMap {

  def apply(prefix: String,
            kafkaProperty: KafkaPrdProperty)(
             implicit appProps: AllApplicationProperties
           ): KafkaProducerPropertyMap = {
    prefix createProperty { prf =>
      KafkaProducerPropertyMap(
        producers = mapProperty(prf, { (str, appProps) => KafkaProducerProperty(str, kafkaProperty)(appProps) })
      )
    }
  }

}
