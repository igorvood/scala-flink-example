package ru.vood.flink.configuration.example

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.Semantic
import ru.vood.flink.configuration.AllApplicationProperties
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
  def apply(prf: String,
            kafkaProperty: KafkaProperty)(
             implicit appProps: AllApplicationProperties
           ): KafkaProducerProperty = new KafkaProducerProperty(
    producerSemantic = Semantic.valueOf(propertyVal(prf, "producerSemantic")),
    topicName = propertyVal(prf, "topicName"),
    propertiesProducers = kafkaProperty.property.clone.asInstanceOf[Properties]
  )

}