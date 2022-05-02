package ru.vood.flink.dto

case class UniversalDto(dataString: Map[String, String],
                        dataBigDecimal: Map[String, BigDecimal],
                        dataBoolean: Map[String, Boolean],
                        uuid: String
                       )
