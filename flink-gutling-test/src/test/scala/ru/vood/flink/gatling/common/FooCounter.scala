package ru.vood.flink.gatling.common

import java.util.concurrent.atomic.AtomicLong

class FooCounter(initialValue:Long) {
  val counter = new AtomicLong(initialValue)
  def get():Long = counter.get()
  def set(v: Long): Unit = counter.set(v)
  def inc(): Long = counter.incrementAndGet()
  def modify(f: Long => Long): Unit = {
    var done = false
    var oldVal: Long = 0
    while (!done) {
      oldVal = counter.get()
      done = counter.compareAndSet(oldVal, f(oldVal))
    }
  }
}