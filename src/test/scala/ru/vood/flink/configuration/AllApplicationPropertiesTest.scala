package ru.vood.flink.configuration

import ru.vood.flink.configuration.example.{KafkaProducerPropertyMap, KafkaProperty}

class AllApplicationPropertiesTest extends org.scalatest.flatspec.AnyFlatSpec {


  "read property" should " be success " in {

    implicit val properties: AllApplicationProperties = ConfigUtils.getAllProps(fileNames = List("KafkaProducerPropertyMap.properties"))

    val kafkaProperty = KafkaProperty("kafka.producer.common")

    val kafkaProducerPropertyMap = KafkaProducerPropertyMap("kafka.producer.custom", kafkaProperty)

    println(kafkaProperty)
  }

}
