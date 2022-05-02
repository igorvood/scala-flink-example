package ru.vood.flink.job

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.ConfigUtils.{getPropsFromArgs, getPropsFromResourcesFile}

import scala.util.{Failure, Success}

trait JobInterface[T, CONFIGURATION] {


  implicit def configApp(args: Array[String])(implicit argsToProp: Array[String] => AllApplicationProperties): CONFIGURATION

  def init(env: StreamExecutionEnvironment)(implicit f: SourceFunction[T]): DataStream[T]

  def process(dataStream: DataStream[T])(implicit filterConfiguration: CONFIGURATION): DataStream[T]

  def setMainSink(mainDataStream: DataStream[T])(implicit configuration: CONFIGURATION): Unit

  def runFlow(env: StreamExecutionEnvironment, cfg: CONFIGURATION)(implicit f: SourceFunction[T]): Unit = {
    val dataStream = init(env)
    val processedDataStream = process(dataStream)(cfg)
    setMainSink(processedDataStream)(cfg)
  }


  implicit def initAllProps: Array[String] => AllApplicationProperties = { args =>
    val argsProps = getPropsFromArgs(args).get
    val appProps = getPropsFromResourcesFile("application.properties").get
    val appPropsLocal: Map[String, String] = getPropsFromResourcesFile("application-local.properties") match {
      case Success(x) => x
      case Failure(_) => Map()
    }
    AllApplicationProperties(appPropsLocal ++ appProps ++ argsProps)
  }


}
