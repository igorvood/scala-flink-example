package ru.vood.flink.simple.test

import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.scalatest.matchers.should

import java.util.Properties
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

class SimpleFlinkKafkaTest extends org.scalatest.flatspec.AnyFlatSpec with should.Matchers with EmbeddedKafka {

  "runs with embedded kafka" should "work" in {

    implicit val config = EmbeddedKafkaConfig(
      kafkaPort = SimpleFlinkKafkaTest.kafkaPort,
      zooKeeperPort = SimpleFlinkKafkaTest.zooKeeperPort
    )

    withRunningKafka {

      publishStringMessageToKafka("input-topic", SimpleFlinkKafkaTest.inputString)

      val env = StreamExecutionEnvironment.getExecutionEnvironment

      env.setParallelism(1)

      val kafkaConsumer = new FlinkKafkaConsumer(
        "input-topic",
        new SimpleStringSchema,
        SimpleFlinkKafkaTest.props
      )

      implicit val typeInfo = TypeInformation.of(classOf[String])

      val inputStream = env.addSource(kafkaConsumer)

      val outputStream = inputStream.map(_.toUpperCase)

      val kafkaProducer = new FlinkKafkaProducer(
        "output-topic",
        new SimpleStringSchema(),
        SimpleFlinkKafkaTest.props
      )
      outputStream.addSink(kafkaProducer)
      env.execute()
      consumeFirstStringMessageFrom("output-topic") shouldEqual SimpleFlinkKafkaTest.expectedString

    }

  }
}

