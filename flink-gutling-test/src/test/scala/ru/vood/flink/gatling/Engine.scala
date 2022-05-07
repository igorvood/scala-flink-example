package ru.vood.flink.gatling

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder
import ru.vood.flink.gatling.base.IDEPathHelper

object Engine extends App {

  val props = new GatlingPropertiesBuilder()
    .resourcesDirectory(IDEPathHelper.mavenResourcesDirectory.toString)
    .resultsDirectory(IDEPathHelper.resultsDirectory.toString)
    .binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString)
    .simulationClass("ru.vtb.ie.test.UaspStreamingBussinesRulesIntegrationTestScript")

  Gatling.fromMap(props.build)
}
