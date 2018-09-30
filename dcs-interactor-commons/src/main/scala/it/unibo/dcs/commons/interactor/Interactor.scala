package it.unibo.dcs.commons.interactor

import rx.lang.scala.{Observable, Subscriber}

trait Interactor[T, P] {

  def apply(parameters: P, subscriber: Subscriber[T]): Unit

  def apply(parameters: P): Observable[T]

}
