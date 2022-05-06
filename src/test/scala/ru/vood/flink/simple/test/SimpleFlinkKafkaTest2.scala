package ru.vood.flink.simple.test

//import net.mguenther.kafka.junit.EmbeddedKafkaCluster.provisionWith
//import net.mguenther.kafka.junit.EmbeddedKafkaClusterConfig.useDefaults
//import net.mguenther.kafka.junit.ObserveKeyValues
import org.scalatest.matchers.should
//import net.mguenther.kafka.junit.SendValues

class SimpleFlinkKafkaTest2 extends org.scalatest.flatspec.AnyFlatSpec with should.Matchers {

  "runs with embedded kafka" should "work" in {
/*
    val kafka = provisionWith(useDefaults())

    kafka.start


/*
    val value1 = SendValues.to("test-topic", "A").build()
    kafka.send(value1)
    //    kafka.send(to("test-topic", "a", "b", "c"))
    kafka.observe(ObserveKeyValues.on("test-topic", 3).build())
*/
    kafka.stop()
*/
  }
}
