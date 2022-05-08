package ru.vood.flink.gatling.constructor.abstractscenario

import io.gatling.core.protocol.Protocol
import ru.vood.flink.gatling.config.AdditionalProducerGatlingProp

trait GatlingProtocol {

  def createProtocol: Protocol

}
