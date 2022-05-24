package ru.vood.flink.filter

import play.api.libs.json.{Json, OWrites}

import java.lang

sealed trait OperandClass{

 /* implicit val writesLongOperand: OWrites[LongOperand] = Json.writes[LongOperand]
  implicit val writesIntOperand: OWrites[IntOperand] = Json.writes[IntOperand]
  implicit val writesFloatOperand: OWrites[FloatOperand] = Json.writes[FloatOperand]
  implicit val writesDoubleOperand: OWrites[DoubleOperand] = Json.writes[DoubleOperand]
  implicit val writesBigDecimalOperand: OWrites[BigDecimalOperand] = Json.writes[BigDecimalOperand]
  implicit val writesStringOperand: OWrites[StringOperand] = Json.writes[StringOperand]
  implicit val writesBooleanOperand: OWrites[BooleanOperand] = Json.writes[BooleanOperand]*/
}

object OperandClass{

  implicit val writes: OWrites[OperandClass] = OWrites {
    case LongOperand(value) => Json.obj("LongOperand" -> value.toString)
    case IntOperand(value) => Json.obj("IntOperand" -> value.toString)
    case FloatOperand(value) => Json.obj("FloatOperand" -> value.toString)
    case DoubleOperand(value) => Json.obj("DoubleOperand" -> value.toString)
    case BigDecimalOperand(value) => Json.obj("BigDecimalOperand" -> value.toString)
    case StringOperand(value) => Json.obj("StringOperand" -> value)
    case BooleanOperand(value) => Json.obj("BooleanOperand" -> value.toString)
  }
}

case class LongOperand(value: Option[lang.Long]) extends OperandClass

case class IntOperand(value: Option[Integer]) extends OperandClass

case class FloatOperand(value: Option[lang.Float]) extends OperandClass

case class DoubleOperand(value: Option[lang.Double]) extends OperandClass

case class BigDecimalOperand(value: Option[BigDecimal]) extends OperandClass

case class StringOperand(value: Option[String]) extends OperandClass

case class BooleanOperand(value: Option[lang.Boolean]) extends OperandClass
