package it.unibo.dcs.commons

import io.vertx.core.{AsyncResult, Handler}
import io.vertx.lang.scala.HandlerOps.handlerForAsyncResultWithConversion
import it.unibo.dcs.commons.VertxUtils.Implicits.handlerToFunction

import scala.concurrent.Future
import scala.language.implicitConversions

object VertxUtils {

  def toFuture[T](action: (AsyncResult[T] => Unit) => Unit): Future[T] = toFuture[T, T](identity)(action)

  def toFuture[J, S](converter: J => S)(action: (AsyncResult[J] => Unit) => Unit): Future[S] = {
    val (handler, promise) = handlerForAsyncResultWithConversion[J, S](converter)
    action(handler)
    promise.future
  }

  object Implicits {

    implicit def handlerToFunction[T](handler: Handler[T]): Function[T, Unit] = handler.handle

    implicit def functionToHandler[T](handler: Function[T, Any]): Handler[T] = (event: T) => handler(event)

  }

}
