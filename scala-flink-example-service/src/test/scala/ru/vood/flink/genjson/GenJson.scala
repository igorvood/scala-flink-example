package ru.vood.flink.genjson

import org.scalatest.matchers.should
import play.api.libs.json.Json
import ru.vood.flink.filter.dinamic.FilterDto
import ru.vood.flink.filter.{FilterRule, StringOperand}

class GenJson extends org.scalatest.flatspec.AnyFlatSpec with should.Matchers {

  "runs with embedded kafka" should "work" in {
    val dto = FilterDto(
      id = "1",
      isActive = true,
      filterRule = FilterRule("rule_1", fieldName = "fieldName1", StringOperand(Option("aws")), "null"))
    val value1 = Json.toJson(dto)
    val str = Json.prettyPrint(value1)
    println(str)
    assertResult(dto)( Json.fromJson[FilterDto](value1).get)
    

  }
}
