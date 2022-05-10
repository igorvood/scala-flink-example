package ru.vood.flink.gatling.constructor.abstractscenario.layer.first

import io.gatling.core.protocol.Protocol

trait GatlingProtocol {

  def createProtocol: Protocol

}
