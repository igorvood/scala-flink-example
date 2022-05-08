package ru.vood.flink.gatling.abstractscenario

import io.gatling.core.protocol.Protocol

trait GatlingProtocol {

  def createProtocol: Protocol

}
