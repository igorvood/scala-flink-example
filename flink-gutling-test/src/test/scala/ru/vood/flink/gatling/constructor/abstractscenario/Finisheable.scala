package ru.vood.flink.gatling.constructor.abstractscenario

trait Finisheable {
  val scenarioName: String

  def isFinished: Boolean
}
