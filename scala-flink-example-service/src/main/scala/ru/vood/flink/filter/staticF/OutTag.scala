package ru.vood.flink.filter.staticF

import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.OutputTag
import ru.vood.flink.dto.UniversalDto

object OutTag {
  def outputTagsF(prefix: String): OutputTag[UniversalDto] = OutputTag[UniversalDto](outputTagsName(prefix))

  def outputTagsName(prefix: String): String = s"$prefix-success"

  def outputTagsErrsF(prefix: String): OutputTag[UniversalDto] = OutputTag[UniversalDto](outputTagsErrsName(prefix))

  def outputTagsErrsName(prefix: String): String = s"$prefix-error"
}
