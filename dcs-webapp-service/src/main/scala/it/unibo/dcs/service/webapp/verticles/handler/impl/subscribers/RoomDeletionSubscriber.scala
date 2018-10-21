package it.unibo.dcs.service.webapp.verticles.handler.impl.subscribers

import io.vertx.scala.core.Context
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.exceptions._
import rx.lang.scala.Subscriber

final class RoomDeletionSubscriber(private[this] val routingContext: RoutingContext)
                                  (private[this] implicit val ctx: Context) extends Subscriber[Unit] {

  override def onCompleted(): Unit = routingContext response() end

  override def onError(error: Throwable): Unit = error match {

    case TokenCheckResponseException(message) => ???

    case AuthServiceErrorException(errorJson) => ???

    case RoomServiceErrorException(errorJson) => ???

    case RoomDeletionResponseException(message) => ???
  }

}

object RoomDeletionSubscriber {
  def apply(routingContext: RoutingContext)(implicit ctx: Context): RoomDeletionSubscriber =
    new RoomDeletionSubscriber(routingContext)
}

