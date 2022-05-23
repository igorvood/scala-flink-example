package ru.vood.flink.mutation.dto

case class UaspOperation(droolsName: String,
                         typeField: MapClass,
                         nameField: String,
                         typeOperation: TypeOperation,
                        ) {

  def errorOperation: Option[String] =
    (typeOperation, typeField.isNotNull) match {
      case (Add(), true) | (Mutate(), true) | (Delete(), _) => None
      case (ConcatenateStr(_), true) => None
      case (_, _) => Some(s"RuleName '$droolsName': Uncompatitible value '${typeField.value}' for operation $typeOperation ")
    }

  //  require((typeOperation, typeField.isNotNull) match {
  //    case (Add(), true) | (Mutate(), true) | (Delete(), false) => true
  //    case (_, _) => false
  //  }, s"Uncompatitible value $typeOperation and ${typeField.value} ")
}