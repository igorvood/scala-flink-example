package ru.vood.flink.gatling.constructor.data

case class TestCaseData[T](caseName: String,
                           data: T
                          )
