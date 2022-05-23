package ru.vood.flink.filter.staticF

import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, OutputTag, createTypeInformation}
import org.apache.flink.util.Collector
import ru.vood.flink.common.EnrichFlinkDataStream.EnrichFlinkData
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.filter.FilterRule
import ru.vood.flink.filter.staticF.FilterPredef.Predef
import ru.vood.flink.filter.staticF.OutTag.{outputTagsErrsF, outputTagsF}

/*object FilterProcessFunction {

  def apply(prf: String, args: Array[String]): FilterProcessFunction =
    FilterConfigurationUtil.initAllProps(args)
      .also { p => FilterRule(prf)(p) }
      .also { r => new FilterProcessFunction(r) }

}*/

class FilterProcessFunction(private val filterConfig: FilterRule) extends ProcessFunction[UniversalDto, UniversalDto] {

  val outputTags: OutputTag[UniversalDto] = outputTagsF(filterConfig.tagPrefix)
  val outputTagsErrs: OutputTag[UniversalDto] = outputTagsErrsF(filterConfig.tagPrefix)

  override def processElement(value: UniversalDto, ctx: ProcessFunction[UniversalDto, UniversalDto]#Context, out: Collector[UniversalDto]): Unit = {
    val bool = value.filterResult(filterConfig)

    if (bool) {
      out.collect(value)
    } else {
      ctx.output(outputTagsErrs, value)
    }
  }

  def process(inStr: DataStream[UniversalDto]): DataStream[UniversalDto] = {
    inStr.process(processElement)
      .addFlinkMetric(s"${filterConfig.tagPrefix}-before-Filter")
      .enrichName(s"${filterConfig.tagPrefix}-filter")
  }


}
