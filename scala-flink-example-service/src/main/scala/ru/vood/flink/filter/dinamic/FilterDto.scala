package ru.vood.flink.filter.dinamic

import play.api.libs.json.{Json, OWrites}
import ru.vood.flink.filter.FilterRule

case class FilterDto(id: String,
                     isActive: Boolean,
                     filterRule: FilterRule
                    )

object FilterDto {
  implicit val writes: OWrites[FilterDto] = Json.writes[FilterDto]
}