package ru.vood.flink.configuration.example

import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.PropertyUtil.mapProperty

case class KafkaProducerPropertyMap(producers: Map[String, KafkaProducerProperty])


object KafkaProducerPropertyMap {

  def apply(prefix: String,
            kafkaProperty: KafkaProperty)(
             implicit appProps: AllApplicationProperties
           ): KafkaProducerPropertyMap =
    KafkaProducerPropertyMap(
      producers = mapProperty(prefix, { (str, appProps) => KafkaProducerProperty(str, kafkaProperty)(appProps) })
    )


}
