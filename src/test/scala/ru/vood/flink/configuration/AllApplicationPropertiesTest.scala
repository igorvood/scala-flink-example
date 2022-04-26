package ru.vood.flink.configuration

import ru.vood.flink.configuration.example.{KafkaProducerPropertyMap, KafkaProperty}

class AllApplicationPropertiesTest extends org.scalatest.flatspec.AnyFlatSpec {


  private val producerPrefix = "kafka.producer.custom"
  "read property" should " be success " in {

    implicit val properties: AllApplicationProperties = ConfigUtils.getAllProps(fileNames = List("KafkaProducerPropertyMap.properties"))

    val kafkaProperty = KafkaProperty("kafka.producer.common")

    val kafkaProducerPropertyMap = KafkaProducerPropertyMap(producerPrefix, kafkaProperty)

    assertResult(2)(kafkaProducerPropertyMap.producers.size)

  }

  "read property" should " be error " in {

    val properties: AllApplicationProperties = ConfigUtils.getAllProps(fileNames = List("KafkaProducerPropertyMap.properties"))

    val bad = properties.prop.filter(prp => !prp._1.contains(producerPrefix))
    implicit val baseLocalApplicationProperties: AllApplicationProperties = AllApplicationProperties(bad)

    val kafkaProperty = KafkaProperty("kafka.producer.common")

    val assertion = assertThrows[IllegalArgumentException](KafkaProducerPropertyMap(producerPrefix, kafkaProperty))

  }

}
