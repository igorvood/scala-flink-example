package ru.vood.flink.job

import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.PrefixProperty.PredefPrefix
import ru.vood.flink.configuration.PropertyUtil.propertyVal

case class FlinkConfiguration(appStreamCheckpointTimeMilliseconds: Long) {
  private val appStreamCheckpointTimeMillisecondsMin = 10000
  require(appStreamCheckpointTimeMilliseconds >= appStreamCheckpointTimeMillisecondsMin,
    s"appStreamCheckpointTimeMilliseconds must be more than $appStreamCheckpointTimeMillisecondsMin current value is $appStreamCheckpointTimeMilliseconds")
}

object FlinkConfiguration {

  def apply(prefix: String)(implicit appProps: AllApplicationProperties): FlinkConfiguration = {
    prefix createProperty { prf =>
      new FlinkConfiguration(
        appStreamCheckpointTimeMilliseconds = propertyVal(prf, "stream.checkpoint.time.seconds").toLong)

    }
  }

}
