package ru.vood.flink.dto

case class UniversalDto(uuid: String,
                        dataString: Map[String, String],
                        dataBigDecimal: Map[String, BigDecimal],
                        dataBoolean: Map[String, Boolean],
                       )
