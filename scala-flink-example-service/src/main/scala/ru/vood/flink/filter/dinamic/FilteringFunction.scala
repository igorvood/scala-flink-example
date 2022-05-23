package ru.vood.flink.filter.dinamic

import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.scala.OutputTag
import org.apache.flink.util.Collector
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.filter.FilterRule
import ru.vood.flink.filter.staticF.FilterPredef.Predef
import ru.vood.flink.filter.staticF.OutTag.outputTagsErrsF

import java.util
import scala.annotation.tailrec
import scala.collection.JavaConverters._

class FilteringFunction extends BroadcastProcessFunction[UniversalDto, FilterDto, UniversalDto] /*with LazyLogging*/ {

  private val FILTER_STATE_DESCRIPTOR = new MapStateDescriptor(
    "FilterStateDescriptor",
    TypeInformation.of(classOf[String]), TypeInformation.of(classOf[FilterRule]))

  val outputTagsErrs: OutputTag[UniversalDto] = outputTagsErrsF("FilteringFunction")

  @tailrec
  final def runFilter(dto: UniversalDto, filters: Iterable[util.Map.Entry[String, FilterRule]], fl: Boolean): Boolean = {
    if (fl)
      filters match {
        case Nil => fl
        case x :: Nil => dto.filterResult(x.getValue)
        case x :: xs => runFilter(dto, xs, dto.filterResult(x.getValue))
      } else fl
  }

  override def processElement(dto: UniversalDto, ctx: BroadcastProcessFunction[UniversalDto, FilterDto, UniversalDto]#ReadOnlyContext, out: Collector[UniversalDto]): Unit = {
    val filters = ctx.getBroadcastState(FILTER_STATE_DESCRIPTOR)

    if (runFilter(dto, filters.immutableEntries().asScala, fl = true)) {
      out.collect(dto)
    } else {
      ctx.output(outputTagsErrs, dto)
    }

  }

  override def processBroadcastElement(value: FilterDto, ctx: BroadcastProcessFunction[UniversalDto, FilterDto, UniversalDto]#Context, out: Collector[UniversalDto]): Unit = {
    if (value.isActive) upsertFilter(value, ctx) else removeFilter(value, ctx)
  }

  private def upsertFilter(filter: FilterDto, ctx: BroadcastProcessFunction[UniversalDto, FilterDto, UniversalDto]#Context): Unit = {
    val filterState = ctx.getBroadcastState(FILTER_STATE_DESCRIPTOR)
    filterState.put(filter.id, filter.filterRule)
  }

  /**
   * Removes a filter from the state
   */
  private def removeFilter(metricFilter: FilterDto, ctx: BroadcastProcessFunction[UniversalDto, FilterDto, UniversalDto]#Context): Unit = {
    val value = ctx.getBroadcastState(FILTER_STATE_DESCRIPTOR)
    value.remove(metricFilter.id)
  }
}
