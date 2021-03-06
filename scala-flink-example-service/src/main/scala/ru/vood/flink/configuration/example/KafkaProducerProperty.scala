package ru.vood.flink.configuration.example

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.Semantic
import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.PrefixProperty.PredefPrefix
import ru.vood.flink.configuration.PropertyUtil.propertyVal

import java.util.Properties

case class KafkaProducerProperty(producerSemantic: Semantic,
                                 topicName: String,
                                 propertiesProducers: Properties,
                                ) {

  require(topicName.nonEmpty, "topicName must be non empty")

  override def toString: String =
    s"""KafkaProducerProperty{
       |  topicName: $topicName
       |  producerSemantic: $producerSemantic
       |  propertiesProducers: $propertiesProducers
       |}""".stripMargin


}

object KafkaProducerProperty {
  def apply(prefix: String,
            kafkaProperty: KafkaPrdProperty)(
             implicit appProps: AllApplicationProperties
           ): KafkaProducerProperty =
    prefix createProperty { prf =>
      new KafkaProducerProperty(
        producerSemantic = Semantic.valueOf(propertyVal(prf, "producerSemantic")),
        topicName = propertyVal(prf, "topicName"),
        propertiesProducers = kafkaProperty.property.clone.asInstanceOf[Properties]
      )
    }
}