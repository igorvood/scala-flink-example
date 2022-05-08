package ru.vood.flink.gatling

import io.gatling.core.scenario.Simulation
import ru.vood.flink.gatling.config.FlinkGatlingConfig
import ru.vood.flink.gatling.constructor.scenario.SimpleScenario

class FlinkGatlingTestScript extends Simulation {

  implicit val config: FlinkGatlingConfig = FlinkGatlingConfig.apply()

  setUp(
    SimpleScenario().createPopulationBuilder
  )
}
