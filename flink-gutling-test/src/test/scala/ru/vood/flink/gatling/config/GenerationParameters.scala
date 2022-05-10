package ru.vood.flink.gatling.config

import ru.tinkoff.gatling.feeders._
import ru.vood.flink.configuration.AllApplicationProperties
import ru.vood.flink.configuration.PrefixProperty.PredefPrefix
import ru.vood.flink.configuration.PropertyUtil.propertyVal

import scala.collection.immutable
import scala.util.Random

case class GenerationParameters(prefixIdentity: Option[String],
                                countTransaction: Int,
                                countUsers: Int,
                                startUsersIdentity: Long, // = startUserNumberFrom()
                               ) {
  val localCustomerId = "local_customer_id"
  val userCount = 1
  //  val countTransaction = 1
  lazy val ids: Int => immutable.Seq[String] = { cnt =>
    val strings = for (i <- 1 to cnt) yield (i).toString
    strings
  }

  lazy val ids2feeder: IndexedSeq[Map[String, String]] = ids(userCount * countTransaction).toFeeder(localCustomerId)

}

object GenerationParameters {

  def apply(prefix: String)(implicit properties: AllApplicationProperties): GenerationParameters = {
    prefix createProperty { prf =>
      new GenerationParameters(
        prefixIdentity = Option.apply(propertyVal(prf, "prefixIdentity")),
        countTransaction = propertyVal(prf, "countTransaction", "1").toInt,
        countUsers = propertyVal(prf, "countUsers", "1").toInt,
        startUsersIdentity = propertyVal(prf, "startUsersIdentity", startUserNumberFrom().toString).toLong,
      )
    }
  }

  private def startUserNumberFrom(): Long = Random.nextInt(100) * Random.nextInt(100) * 10000
}
