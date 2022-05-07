package ru.vood.flink.configuration

import java.util.Properties
import scala.collection.JavaConverters._

object PropertyUtil extends Serializable {

  def propertyVal(prefix: String, propName: String, defaultVal: String = "")(implicit appPropImplicit: AllApplicationProperties): String =
    filterAndMap(fullPrefix(prefix), appPropImplicit.prop)
      .getOrElse(propName, defaultVal)

  def asProperty(propPrefix: String = "")(implicit appPropImplicit: AllApplicationProperties): Properties = {
    val stringToString = filterAndMap(fullPrefix(propPrefix), appPropImplicit.prop)
    getPropsFromMap(stringToString)
  }

  def fullPrefix(prefix: String): String =
    if (prefix == null || prefix.endsWith("."))
      prefix
    else s"$prefix."

  private def filterAndMap(prf: String, m: Map[String, String]): Map[String, String] = {
    if (prf == null) m
    else m.filter(entry => entry._1.startsWith(prf))
      .map(entry => (entry._1.replace(prf, ""), entry._2))
  }

  def getPropsFromMap(props: Map[String, String]): Properties = {
    val properites = new Properties()
    properites.putAll(props.asJava)
    properites
  }

  def mapProperty[T](prefix: String = "",
                     init: (String, AllApplicationProperties) => T
                    )(implicit appProps: AllApplicationProperties): Map[String, T] = {
    val prf = fullPrefix(prefix)

    val filteredProp = filterAndMap(prf, appProps.prop)
    filteredProp
      .groupBy({ entry => entry._1.split("\\.")(0) })
      .map({ entry =>
        val (key, uncookedVal) = entry
        val t = init(key, AllApplicationProperties(uncookedVal))
        (key, t)
      }
      )
  }


}
