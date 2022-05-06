package ru.vood.flink

import org.apache.flink.api.common.serialization.{AbstractDeserializationSchema, DeserializationSchema}
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.slf4j.LoggerFactory
import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.job.{FlinkJobConfiguration, JobInterface}

object FlinkJob extends JobInterface[UniversalDto, FlinkJobConfiguration] {
  private val logger = LoggerFactory.getLogger(getClass)

  override def init(env: StreamExecutionEnvironment)(implicit f: SourceFunction[UniversalDto]): DataStream[UniversalDto] = env.addSource(f)


  override def defaultConfiguration(implicit allProps: AllApplicationProperties): FlinkJobConfiguration = FlinkJobConfiguration(allProps)

  override def process(dataStream: DataStream[UniversalDto])(implicit filterConfiguration: FlinkJobConfiguration): DataStream[UniversalDto] = {
    dataStream.map(q => q)
  }

  override def setMainSink(mainDataStream: DataStream[UniversalDto])(implicit configuration: FlinkJobConfiguration): Unit = mainDataStream.addSink(configuration.kafkaProducerMap("producer-success"))

  def main(args: Array[String]): Unit = {

    val configuration = configApp(args)

    logger.info("Start app: " + this.getClass.getName)

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    runFlow(env, configuration) { env => init(env)(configuration.kafkaConsumer) }

    env.execute(s"Run job ${getClass.getName}")
  }

}
