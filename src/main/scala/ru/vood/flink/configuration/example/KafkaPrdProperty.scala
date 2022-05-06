package ru.vood.flink.configuration.example

import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.PrefixProperty.PredefPrefix
import ru.vood.flink.configuration.PropertyUtil.{asProperty, fullPrefix}

import java.util.Properties

case class KafkaPrdProperty(property: Properties) {
  override def toString: String =
    s"""KafkaProperty{
       |  property: $property
       |}""".stripMargin

}

object KafkaPrdProperty {
  def apply(prefix: String)(implicit appProps: AllApplicationProperties): KafkaPrdProperty =
    prefix createProperty { prf =>
      new KafkaPrdProperty(asProperty(fullPrefix(prf)))
    }

}