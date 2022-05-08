package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.protocol.Protocol

trait GatlingProtocol {

  def createProtocol: Protocol

}
