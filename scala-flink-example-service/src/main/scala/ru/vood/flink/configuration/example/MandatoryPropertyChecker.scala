package ru.vood.flink.configuration.example

import java.util.Properties

trait MandatoryPropertyChecker {

  val requiredProperty: Set[String]

  def badProperties(property: Properties): String = requiredProperty.map(prop => prop -> property.getProperty(prop)).filter(p => p._2 == null).map(_._1).mkString(",")

  def badProperties(prpMap: Map[String,String]): String = requiredProperty.map(prop => prop -> prpMap.get(prop)).filter(p => p._2.isEmpty).map(_._1).mkString(",")


}
