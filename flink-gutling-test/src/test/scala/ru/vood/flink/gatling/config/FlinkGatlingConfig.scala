package ru.vood.flink.gatling.config

import ru.vood.flink.FlinkJob
import ru.vood.flink.configuration.PrefixProperty.PredefPrefix
import ru.vood.flink.job.FlinkJobConfiguration

case class FlinkGatlingConfig(flinkJobServiceConfiguration: FlinkJobConfiguration,
                              generationParam: GenerationParameters,
                              additionalProducerGatlingProp: AdditionalProducerGatlingProp,
                             ) {


}


object FlinkGatlingConfig extends GatlingConfig {

  private val gatlingPrefix = "gatling."
  val generationParametersPrefix: String = gatlingPrefix + "generation.parameters."

  def apply(prefix: String = gatlingPrefix): FlinkGatlingConfig = {
    val flinkJobServiceConfiguration: FlinkJobConfiguration = FlinkJob.defaultConfiguration
    prefix createProperty { prf =>
      FlinkGatlingConfig(
        flinkJobServiceConfiguration = flinkJobServiceConfiguration,
        generationParam = GenerationParameters(s"${prf}generation.parameters."),
        additionalProducerGatlingProp = AdditionalProducerGatlingProp(s"${gatlingPrefix}additionalProducerGatlingProp")
      )
    }
  }

}