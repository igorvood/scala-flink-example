package ru.vood.flink.configuration

import scala.util.{Failure, Success, Try}

case class PrefixProperty(prf: String) {

  def createPropertyData[T](function: String => T): T = {
    val triedT = Try {
      function(prf)
    } match {
      case Success(value) => value
      case Failure(exception) => throw new IllegalArgumentException("Unable to read property by prefix " + prf, exception)
    }
    triedT
  }

}

