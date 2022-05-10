package ru.vood.flink.gatling.constructor.abstractscenario.layer.second.kafka

import org.apache.kafka.clients.consumer.KafkaConsumer
import ru.vood.flink.configuration.example.KafkaProducerProperty
import ru.vood.flink.gatling.common.FooCounter
import ru.vood.flink.gatling.constructor.abstractscenario.Finisheable

import java.time.Duration
import java.util
import java.util.Properties
import scala.collection.JavaConverters.iterableAsScalaIterableConverter
import scala.jdk.CollectionConverters.asJavaCollectionConverter

class ConsumerService[V](properties: Properties,
                         topics: util.Collection[String])
                        (implicit consumerRecordAccumulator: ConsumerRecordAccumulator[V])
  extends Thread with Finisheable {
  @volatile
  var isEnd = false
  val countMessages: FooCounter = new FooCounter(0)
  val kafkaConsumer: KafkaConsumer[Array[Byte], Array[Byte]] = new KafkaConsumer[Array[Byte], Array[Byte]](properties)
  val flag: FooCounter = new FooCounter(0)

  @volatile
  def isFinished: Boolean = isEnd

  def finish(): Unit = {
    flag.inc()
  }

  def getCountMessages(): Long = {
    countMessages.get()
  }

  def getUsers: Long = consumerRecordAccumulator.getCount

  def get(key: String): V = consumerRecordAccumulator.get(key)

  //  def getAll(key: K):Z = consumerRecordAccumulator.getAll(key)

  override def run(): Unit = {
    try {
      kafkaConsumer.subscribe(topics)
//      println("subscribe: " + topics)
      var i: Long = 0
      while (flag.get() == 0) {
        val records = kafkaConsumer.poll(Duration.ofSeconds(0L))
        for (record <- records.asScala) {
          i = i + 1
          if (consumerRecordAccumulator.addRecord(record)) {
            countMessages.inc()
          }
        }
      }
    } finally {
      isEnd = true
      kafkaConsumer.close()
    }
  }
}

object ConsumerService {

  def factory[V](prop: KafkaProducerProperty)(implicit consumerRecordAccumulator: ConsumerRecordAccumulator[V]): ConsumerService[V] = {
    val transformConsumerService = new ConsumerService(prop.propertiesProducers,
      Seq(prop.topicName).asJavaCollection)
    transformConsumerService.start()
    transformConsumerService
  }

}