package ru.vood.flink.configuration.example

import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.PrefixProperty.PredefPrefix
import ru.vood.flink.configuration.PropertyUtil.{asProperty, fullPrefix}

import java.util.Properties

case class KafkaProperty(property: Properties) {
  override def toString: String =
    s"""KafkaProperty{
       |  property: $property
       |}""".stripMargin

}

object KafkaProperty {
  def apply(prefix: String)(implicit appProps: AllApplicationProperties): KafkaProperty =
    prefix createProperty { prf =>
      new KafkaProperty(asProperty(fullPrefix(prf)))
    }

}
