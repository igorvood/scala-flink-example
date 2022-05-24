package ru.vood.flink.filter

import play.api.libs.json.{Json, OWrites, Reads}
import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.PropertyUtil.propertyVal

case class FilterRule(
                       tagPrefix: String,
                       fieldName: String,
                       //                       compareWith: String,
                       operandClass: OperandClass,
                       operator: String
                     ) {
  require(tagPrefix.nonEmpty, "tagPrefix must be not empty")
  require(fieldName.nonEmpty, "fieldName must be not empty")
  //  require(compareWith.nonEmpty, "compareWith must be not empty")
  require(operator.nonEmpty, "operator must be not empty")
  require(operandClass != null, "operator must be not null")

  def operatorClass[T <: Comparable[T]]: FilterOperator[T] = operator match {
    case "!=" => NotEquals[T]()
    case "=" => Equals[T]()
    case ">" => Grater[T]()
    case ">=" => GraterOrEq[T]()
    case "<" => Less[T]()
    case "<=" => LessOrEq[T]()
    case "null" => Null[T]()
    case "notNull" => NotNull[T]()
    case x => throw new IllegalStateException(s"unsupported operator '$x' ")
  }


  override def toString: String =
    s"""FilterRule{
       |  fieldName = $fieldName
       |  tagPrefix = $tagPrefix
       |  operatorClass = ${operatorClass.getClass}
       |  operandClass = ${operandClass.getClass}
       |}""".stripMargin

}

object FilterRule {

  def apply(prf: String)(implicit appProps: AllApplicationProperties): FilterRule =
    new FilterRule(
      tagPrefix = propertyVal(prf, "tagPrefix"),
      fieldName = propertyVal(prf, "fieldName"),
      //      compareWith = propertyVal(prf, "compareWith"),
      operandClass = getOperatorClass(propertyVal(prf, "operandClass"), propertyVal(prf, "compareWith")),
      operator = propertyVal(prf, "operator")
    )

  def getOperatorClass(clazz: String, value: String): OperandClass =
    clazz
    match {
      case "Int" => IntOperand(Option(value).map(_.toInt))
      case "Long" => LongOperand(Option(value).map(_.toLong))
      case "Float" => FloatOperand(Option(value).map(_.toFloat))
      case "Double" => DoubleOperand(Option(value).map(_.toDouble))
      case "BigDecimal" => BigDecimalOperand(Option(value).map(BigDecimal(_)))
      case "String" => StringOperand(Option(value))
      case "Boolean" => BooleanOperand(Option(value).map(_.toBoolean))
      case unknown => throw new IllegalStateException(s"unsupported operandClass '$unknown' for prefix '$clazz'")
    }

  implicit val writes: OWrites[FilterRule] = Json.writes[FilterRule]

  implicit val reads: Reads[FilterRule] = Json.reads[FilterRule]
}

