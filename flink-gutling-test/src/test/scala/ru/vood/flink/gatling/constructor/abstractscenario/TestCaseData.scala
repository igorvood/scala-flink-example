package ru.vood.flink.gatling.constructor.abstractscenario

case class TestCaseData[T](caseName: String,
                           data: T
                          )
