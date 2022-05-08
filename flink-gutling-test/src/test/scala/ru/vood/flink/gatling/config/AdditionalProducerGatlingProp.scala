package ru.vood.flink.gatling.config

import org.apache.kafka.clients.producer.ProducerConfig.{KEY_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER_CLASS_CONFIG}
import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.PrefixProperty.PredefPrefix
import ru.vood.flink.configuration.PropertyUtil.mapProperty
import ru.vood.flink.configuration.example.MandatoryPropertyChecker

case class AdditionalProducerGatlingProp(private val adds: Map[String, String]) extends MandatoryPropertyChecker {

  override val requiredProperty: Set[String] = Set(
    KEY_SERIALIZER_CLASS_CONFIG.replace(".","_"),
    VALUE_SERIALIZER_CLASS_CONFIG.replace(".","_")
  )

  println(requiredProperty)
  println(requiredProperty)
  require(badProperties(adds) == "", s"Properties ${badProperties(adds)} must be not null")

  def addPrp : Map[String, String] = adds.map(q=>q._1.replace("_",".") -> q._2)

}

object AdditionalProducerGatlingProp {

  def apply(prefix: String)(
    implicit appProps: AllApplicationProperties
  ): AdditionalProducerGatlingProp = {
    prefix createProperty { prf =>
      AdditionalProducerGatlingProp(
        adds = mapProperty(prf, { (str, appProps) => appProps.prop(str) })
      )
    }
  }

}