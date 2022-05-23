package ru.vood.flink.mutation

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.streaming.api.scala.{DataStream, createTypeInformation}
import ru.vood.flink.common.CommonExtension.Also
import ru.vood.flink.common.EnrichFlinkDataStream.EnrichFlinkData
import ru.vood.flink.dto.UniversalDto
import ru.vood.flink.mutation.BusinessRulesService.errFieldName
import ru.vood.flink.mutation.drools.{DroolsRunner, KieBaseService}
import ru.vood.flink.mutation.dto._

import scala.annotation.tailrec
import scala.collection.{Set, mutable}
import scala.util.{Failure, Success, Try}

class BusinessRulesService(drl: UniversalDto => Set[UaspOperation]) extends MapFunction[UniversalDto, UniversalDto] {

  def process(inStream: DataStream[UniversalDto]): DataStream[UniversalDto] =
    inStream
      .map(this)
      .enrichName("BusinessRulesService-output")
      .addFlinkMetric("BusinessRulesService-cnt-uap")


  override def map(value: UniversalDto): UniversalDto = {
    val mutateSet = drl(value)
    validateOperation(mutateSet)
    val triedDto = Try {
      recursiveMutate(value, mutateSet.toList)
    }
    triedDto match {
      case Success(u) => u
      case Failure(exception) =>
        recursiveMutate(value, List(UaspOperation("put error", StringMap(exception.getMessage), errFieldName, Add())))
    }
  }

  @tailrec private def recursiveMutate(uaspDto: UniversalDto, mutate: List[UaspOperation]): UniversalDto =
    mutate match {
      case Nil => uaspDto
      case x :: Nil => recursiveMutate(mutateFun(uaspDto, x), Nil)
      case x :: xs => recursiveMutate(mutateFun(uaspDto, x), xs)
    }

  private def mutateFun(value: UniversalDto, m: UaspOperation) =
    m.typeField match {
      //      case LongMap(q) => value.copy(dataLong = m.typeOperation.mutate(value.dataLong, Option(q), m.nameField))
      //      case IntMap(q) => value.copy(dataInt = m.typeOperation.mutate(value.dataInt, Option(q), m.nameField))
      //      case FloatMap(q) => value.copy(dataFloat = m.typeOperation.mutate(value.dataFloat, Option(q), m.nameField))
      //      case DoubleMap(q) => value.copy(dataDouble = m.typeOperation.mutate(value.dataDouble, Option(q), m.nameField))
      case BigDecimalMap(q) => value.copy(dataBigDecimal = m.typeOperation.mutate(value.dataBigDecimal, Option(q), m.nameField))
      case StringMap(q) => value.copy(dataString = m.typeOperation.mutate(value.dataString, Option(q), m.nameField))
      case BooleanMap(q) => value.copy(dataBoolean = m.typeOperation.mutate(value.dataBoolean, Option(q), m.nameField))
    }

  private def validateOperation(mutateSet: Set[UaspOperation]) = {
    val dublicateFields = mutateSet
      .filter(_.typeOperation.mustBeSingle)
      .foldLeft(mutable.Map[(String, Class[_ <: MapClass]), List[UaspOperation]]())({ (resMap, oper) =>
        val key = (oper.nameField, oper.typeField.getClass)
        val newList =
          resMap.get(key)
            .map(l => l :+ oper)
            .getOrElse(List(oper))
        resMap.put(key, newList)
        resMap
      })
      .filter(_._2.size > 1)

    require(dublicateFields.isEmpty, {
      s"drools should not mutate same field $dublicateFields "
    })

    val error = mutateSet.flatMap(w => w.errorOperation).mkString(sep = ",")

    require(error.isEmpty, error)
  }


}

object BusinessRulesService {
  val errFieldName: String = "sys-BussinesRulles-error"

  def apply(kbPaths: List[String]): BusinessRulesService = {
    val service = new KieBaseService(kbPaths)
      .also { q => DroolsRunner(q.kBase) }
      .also { droolsRunner =>
        new BusinessRulesService({ q =>
          droolsRunner
            .apply(q, { case x: UaspOperation => x })
        })
      }
    service
  }
}
