package ru.vood.flink.filter.dinamic

import ru.vood.flink.filter.FilterRule

case class FilterDto(id: String,
                     isActive: Boolean,
                     filterRule: FilterRule
                    )
