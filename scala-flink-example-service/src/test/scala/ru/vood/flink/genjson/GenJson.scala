package ru.vood.flink.genjson

import org.scalatest.matchers.should
import play.api.libs.json.Json
import ru.vood.flink.filter.dinamic.FilterDto
import ru.vood.flink.filter.{FilterRule, StringOperand}

class GenJson extends org.scalatest.flatspec.AnyFlatSpec with should.Matchers {

  "runs with embedded kafka" should "work" in {
    val value1 = Json.toJson(FilterDto(
      id = "1",
      isActive = true,
      filterRule = FilterRule("qw", fieldName = "fieldName1", StringOperand(Option("aws")), "null")))
    val str = Json.prettyPrint(value1)
    println(str)

  }
}
