package ru.vood.flink.gatling.constructor.abstractscenario

trait SessionParamNamesConvensions {

  protected lazy val testCaseName = "testCaseName"

  protected lazy val inputIdDtoSessionName = "customer_id"
  protected lazy val bytesInputDtoSessionName = "bytes_uaspDto"

  protected lazy val countMessages = "countMessages"

  val localCustomerId = "local_customer_id"

}
