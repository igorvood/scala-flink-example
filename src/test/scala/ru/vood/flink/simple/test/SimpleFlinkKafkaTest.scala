package ru.vood.flink.simple.test

import java.util.Properties

import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer011, FlinkKafkaProducer011}
import org.scalatest.matchers.should.{Matchers, WordSpec}

import scala.collection.mutable.ListBuffer

object SimpleFlinkKafkaTest {

  class CollectSink extends SinkFunction[String] {
    override def invoke(string: String): Unit = {
      synchronized {
        CollectSink.values += string
      }
    }
  }

  object CollectSink {
    val values: ListBuffer[String] = ListBuffer.empty[String]
  }

  val kafkaPort = 9092
  val zooKeeperPort = 2181

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:" + kafkaPort.toString)
  props.put("schema.registry.url", "localhost:" + zooKeeperPort.toString)

  val inputString = "mystring"
  val expectedString = "MYSTRING"
}