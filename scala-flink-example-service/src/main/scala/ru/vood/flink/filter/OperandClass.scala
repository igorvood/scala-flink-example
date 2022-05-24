package ru.vood.flink.filter

import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, OWrites, Reads, __}

import java.lang

sealed trait OperandClass {

  val typeValue: String = this.getClass.getSimpleName

}

case class te(a1: String, string: String)

object OperandClass {

  implicit val writes: OWrites[OperandClass] = OWrites {
    case LongOperand(value) => Json.obj("type" -> "LongOperand", "value" -> value.toString)
    case IntOperand(value) => Json.obj("type" -> "IntOperand", "value" -> value.toString)
    case FloatOperand(value) => Json.obj("type" -> "FloatOperand", "value" -> value.toString)
    case DoubleOperand(value) => Json.obj("type" -> "DoubleOperand", "value" -> value.toString)
    case BigDecimalOperand(value) => Json.obj("type" -> "BigDecimalOperand", "value" -> value.toString)
    case StringOperand(value) => Json.obj("type" -> "StringOperand", "value" -> value)
    case BooleanOperand(value) => Json.obj("type" -> "BooleanOperand", "value" -> value.toString)
  }

  implicit val reads: Reads[OperandClass] = (
    (__ \ "type").read[String] and
      (__ \ "value").readNullable[String])
    .tupled
    .map(t => t._1 match {
      case "LongOperand" => LongOperand(t._2.map(_.toLong))
      case "IntOperand" => IntOperand(t._2.map(_.toInt))
      case "FloatOperand" => FloatOperand(t._2.map(_.toFloat))
      case "DoubleOperand" => DoubleOperand(t._2.map(_.toDouble))
      case "BigDecimalOperand" => BigDecimalOperand(t._2.map(BigDecimal(_)))
      case "StringOperand" => StringOperand(t._2)
      case "BooleanOperand" => BooleanOperand(t._2.map(_.toBoolean))
    })

}

case class LongOperand(value: Option[lang.Long]) extends OperandClass

/*object LongOperand{
  implicit val writes: OWrites[LongOperand] = Json.writes[LongOperand]
  implicit val reads: Reads[LongOperand] = Json.reads[LongOperand]
}*/

case class IntOperand(value: Option[Integer]) extends OperandClass
/*object IntOperand{
  implicit val writes: OWrites[IntOperand] = Json.writes[IntOperand]
  implicit val reads: Reads[IntOperand] = Json.reads[IntOperand]
}*/

case class FloatOperand(value: Option[lang.Float]) extends OperandClass
/*object FloatOperand{
  implicit val writes: OWrites[FloatOperand] = Json.writes[FloatOperand]
  implicit val reads: Reads[FloatOperand] = Json.reads[FloatOperand]
}*/

case class DoubleOperand(value: Option[lang.Double]) extends OperandClass
/*object DoubleOperand{
  implicit val writes: OWrites[DoubleOperand] = Json.writes[DoubleOperand]
  implicit val reads: Reads[DoubleOperand] = Json.reads[DoubleOperand]
}*/

case class BigDecimalOperand(value: Option[BigDecimal]) extends OperandClass
/*
object BigDecimalOperand{
  implicit val writes: OWrites[BigDecimalOperand] = Json.writes[BigDecimalOperand]
  implicit val reads: Reads[BigDecimalOperand] = Json.reads[BigDecimalOperand]
}
*/

case class StringOperand(value: Option[String]) extends OperandClass
/*object StringOperand{
  implicit val writes: OWrites[StringOperand] = Json.writes[StringOperand]
  implicit val reads: Reads[StringOperand] = Json.reads[StringOperand]
}*/

case class BooleanOperand(value: Option[lang.Boolean]) extends OperandClass

/*
object BooleanOperand{
  implicit val writes: OWrites[BooleanOperand] = Json.writes[BooleanOperand]
  implicit val reads: Reads[BooleanOperand] = Json.reads[BooleanOperand]
}*/
