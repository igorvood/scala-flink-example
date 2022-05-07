package ru.vood.flink.configuration

import ru.vood.flink.configuration.example.{KafkaPrdProperty, KafkaProducerPropertyMap}

class AllApplicationPropertiesTest extends org.scalatest.flatspec.AnyFlatSpec {


  private val producerPrefix = "kafka.producer.custom"
  "read property from property file" should " be success " in {

    implicit val properties: AllApplicationProperties = ConfigUtils.getAllProps(fileNames = List("KafkaProducerPropertyMap.properties"))

    val kafkaProperty = KafkaPrdProperty("kafka.producer.common")

    val kafkaProducerPropertyMap = KafkaProducerPropertyMap(producerPrefix, kafkaProperty)

    assertResult(2)(kafkaProducerPropertyMap.producers.size)

  }

  "read property from property file" should " be error " in {

    val properties: AllApplicationProperties = ConfigUtils.getAllProps(fileNames = List("KafkaProducerPropertyMap.properties"))

    val bad = properties.prop.filter(prp => !prp._1.contains(producerPrefix))
    implicit val baseLocalApplicationProperties: AllApplicationProperties = AllApplicationProperties(bad)

    val kafkaProperty = KafkaPrdProperty("kafka.producer.common")

    assertThrows[IllegalArgumentException](KafkaProducerPropertyMap(producerPrefix, kafkaProperty))

  }

}
