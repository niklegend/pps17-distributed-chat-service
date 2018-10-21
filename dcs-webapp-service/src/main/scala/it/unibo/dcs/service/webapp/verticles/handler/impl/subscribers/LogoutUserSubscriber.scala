package it.unibo.dcs.service.webapp.verticles.handler.impl.subscribers

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.scala.core.http.HttpServerResponse
import it.unibo.dcs.exceptions.ErrorSubscriber

import scala.language.postfixOps
import it.unibo.dcs.commons.VertxWebHelper.Implicits.RichHttpServerResponse
import rx.lang.scala.Subscriber

final class LogoutUserSubscriber(protected override val response: HttpServerResponse) extends Subscriber[Unit]
  with ErrorSubscriber {

  override def onCompleted(): Unit = response.setStatus(HttpResponseStatus.RESET_CONTENT).end()

}

object LogoutUserSubscriber {

  def apply(response: HttpServerResponse): LogoutUserSubscriber =
    new LogoutUserSubscriber(response)
}