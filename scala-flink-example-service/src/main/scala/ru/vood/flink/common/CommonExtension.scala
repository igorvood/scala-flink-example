package ru.vood.flink.common

object CommonExtension {
  implicit class Also[T](val obj: T) {
    def also[O](func: T => O) : O = func(obj)
  }
}
