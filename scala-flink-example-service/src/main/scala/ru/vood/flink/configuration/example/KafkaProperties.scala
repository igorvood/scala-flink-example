package ru.vood.flink.configuration.example

import java.util.Properties

trait KafkaProperties {

  val requiredProperty: Set[String]

  def badProperties(property: Properties): String = requiredProperty.map(prop => prop -> property.getProperty(prop)).filter(p => p._2 == null).map(_._1).mkString(",")


}
