package ru.vood.flink.configuration.example

import ru.vood.flink.configuration.PropertyUtil.{asProperty, fullPrefix}
import ru.vood.flink.configuration.{AllApplicationProperties, PrefixProperty}

import java.util.Properties

case class KafkaProperty(property: Properties) {
  override def toString: String =
    s"""KafkaProperty{
       |  property: $property
       |}""".stripMargin

}

object KafkaProperty {
  def apply(prefix: String)(implicit appProps: AllApplicationProperties): KafkaProperty =
    PrefixProperty(prefix)
      .createPropertyData { prf =>
        new KafkaProperty(asProperty(fullPrefix(prf)))
      }

}
