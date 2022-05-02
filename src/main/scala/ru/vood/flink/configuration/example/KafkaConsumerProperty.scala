package ru.vood.flink.configuration.example

import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.PrefixProperty.PredefPrefix
import ru.vood.flink.configuration.PropertyUtil.propertyVal

import java.util.Properties

case class KafkaConsumerProperty(topicName: String,
                                 propertiesConsumer: Properties
                                ) {
  require(topicName.nonEmpty, "topicName must be not empty")

  override def toString: String =
    s"""KafkaConsumerProperty{
       |  topicName: $topicName
       |  propertiesConsumer: $propertiesConsumer
       |}""".stripMargin

}

object KafkaConsumerProperty {

  def apply(prefix: String,
            kafkaProperty: KafkaCnsProperty)(implicit appProps: AllApplicationProperties): KafkaConsumerProperty =
    prefix createProperty { prf =>
      KafkaConsumerProperty(topicName = propertyVal(prf, "topicName"),
        propertiesConsumer = kafkaProperty.property.clone.asInstanceOf[Properties]
      )
    }


}