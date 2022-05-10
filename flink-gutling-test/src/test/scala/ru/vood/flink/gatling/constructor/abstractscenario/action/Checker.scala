package ru.vood.flink.gatling.constructor.abstractscenario.action

import io.gatling.core.session.Session

trait Checker {
  def check(session: Session): CheckData
}
