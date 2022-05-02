package ru.vood.flink.configuration.example

import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.PrefixProperty.PredefPrefix
import ru.vood.flink.configuration.PropertyUtil.{asProperty, fullPrefix}

import java.util.Properties

case class KafkaCnsProperty(property: Properties) {
  override def toString: String =
    s"""KafkaProperty{
       |  property: $property
       |}""".stripMargin

}

object KafkaCnsProperty {
  def apply(prefix: String)(implicit appProps: AllApplicationProperties): KafkaCnsProperty =
    prefix createProperty { prf =>
      new KafkaCnsProperty(asProperty(fullPrefix(prf)))
    }

}
