package ru.vood.flink.configuration

import ru.vtb.uasp.common.utils.config.ConfigUtils.getPropsFromMap

import java.util.Properties

object PropertyUtil extends Serializable {

  def propertyVal(prefix: String, propName: String, defaultVal: String = "")(implicit appPropImplicit: AllApplicationProperties): String =
    filterAndMap(fullPrefix(prefix), appPropImplicit.prop)
      .getOrElse(propName, defaultVal)

  def fullPrefix(prefix: String): String =
    if (prefix == null || prefix.endsWith("."))
      prefix
    else s"$prefix."

  private def filterAndMap(prf: String, m: Map[String, String]): Map[String, String] = {
    if (prf == null) m
    else m.filter(entry => entry._1.startsWith(prf))
      .map(entry => (entry._1.replace(prf, ""), entry._2))
  }

  def asProperty(propPrefix: String = "")(implicit appPropImplicit: AllApplicationProperties): Properties = {
    val stringToString = filterAndMap(fullPrefix(propPrefix), appPropImplicit.prop)
    getPropsFromMap(stringToString)
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
