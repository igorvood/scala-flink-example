package ru.vood.flink.gatling.config

import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.ConfigUtils.getPropsFromResourcesFile

import scala.util.{Failure, Success}

trait GatlingConfig {
  private val sysEnv: Map[String, String] = sys.env

  implicit def initAllProps: AllApplicationProperties = {
    val argsProps = sysEnv
    val appProps = getPropsFromResourcesFile("application.properties").get
    val appPropsLocal: Map[String, String] = getPropsFromResourcesFile("application-local.properties") match {
      case Success(x) => x
      case Failure(_) => Map()
    }
    val gatlingLocal: Map[String, String] = getPropsFromResourcesFile("application-gatling-local.properties") match {
      case Success(x) => x
      case Failure(_) => Map()
    }

    AllApplicationProperties(appPropsLocal ++ appProps ++ gatlingLocal ++ argsProps)
  }
}
