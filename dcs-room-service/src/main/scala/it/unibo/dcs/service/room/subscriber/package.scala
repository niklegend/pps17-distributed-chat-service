package it.unibo.dcs.service.room

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.http.HttpServerResponse
import it.unibo.dcs.commons.VertxWebHelper.Implicits.{jsonObjectToString, RichHttpServerResponse}
import it.unibo.dcs.exceptions.ErrorSubscriber
import rx.lang.scala.Subscriber

package object subscriber {

  final class CreateRoomSubscriber (protected override val response: HttpServerResponse) extends Subscriber[Unit]
    with ErrorSubscriber {

    override def onNext(value: Unit): Unit = ()

    override def onCompleted(): Unit = response.setStatus(HttpResponseStatus.CREATED).end()

  }

  final class CreateUserSubscriber(protected override val response: HttpServerResponse) extends Subscriber[Unit]
    with ErrorSubscriber {

    override def onNext(value: Unit): Unit = ()

    override def onCompleted(): Unit = response.setStatus(HttpResponseStatus.CREATED).end()

  }

  class DeleteRoomSubscriber(protected override val response: HttpServerResponse) extends Subscriber[String]
    with ErrorSubscriber {

    override def onNext(name: String): Unit = response.setStatus(HttpResponseStatus.OK).end(Json.obj(("name", name)))

  }

}
