package it.unibo.dcs.commons

import io.vertx.core.{AsyncResult, Handler}
import it.unibo.dcs.commons.RxHelper.Implicits._
import it.unibo.dcs.commons.VertxHelper.Implicits._
import rx.functions.Func1
import rx.lang.scala.Observable

object VertxHelper {

  def toObservable[T](action: (AsyncResult[T] => Unit) => Unit): Observable[T] = toObservable[T, T](identity)(action)

  def toObservable[J, S](converter: J => S)(action: (AsyncResult[J] => Unit) => Unit): Observable[S] = {
    val observable = io.vertx.rx.java.RxHelper.observableFuture[J]()
    action(observable.toHandler())
    observable.map[S](converter)
  }

  object Implicits {

    implicit def func1ToFunction[T, R](func: Func1[T, R]): Function[T, R] = func.call

    implicit def handlerToFunction[T](handler: Handler[T]): Function[T, Unit] = handler.handle

    implicit def functionToHandler[T](handler: Function[T, Any]): Handler[T] = (event: T) => handler(event)

  }

}
