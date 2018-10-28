package it.unibo.dcs.service.room

import io.vertx.lang.scala.ScalaLogger
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.http.HttpServerResponse
import it.unibo.dcs.commons.JsonHelper
import it.unibo.dcs.commons.VertxWebHelper.Implicits.jsonObjectToString
import it.unibo.dcs.exceptions.ErrorSubscriber
import it.unibo.dcs.service.room.model.{Participation, Room}
import it.unibo.dcs.service.room.subscriber.Implicits._
import rx.lang.scala.Subscriber

import scala.language.implicitConversions
package object subscriber {

  final class CreateRoomSubscriber(protected override val response: HttpServerResponse) extends Subscriber[Room]
    with ErrorSubscriber {

    private[this] val log = ScalaLogger.getLogger(getClass.getName)

    override def onNext(room: Room): Unit = {
      val json: JsonObject = room
      log.info(s"Answering with room: $json")
      response.end(json)
    }

  }

  final class JoinRoomSubscriber(protected override val response: HttpServerResponse) extends Subscriber[Participation]
    with ErrorSubscriber {

    private[this] val log = ScalaLogger.getLogger(getClass.getName)

    override def onNext(participation: Participation): Unit = {
      val json: JsonObject = participation
      log.info(s"Answering with participation: $json")
      response.end(json)
    }
  }

  final class CreateUserSubscriber(protected override val response: HttpServerResponse) extends Subscriber[Unit]
    with ErrorSubscriber {

    override def onCompleted(): Unit = response.end()

  }

  class DeleteRoomSubscriber(protected override val response: HttpServerResponse) extends Subscriber[String]
    with ErrorSubscriber {

    override def onNext(name: String): Unit = response.end(Json.obj(("name", name)))

  }

  object Implicits {

    implicit def roomToJsonObject(room: Room): JsonObject = JsonHelper.toJsonObject(gson, room)

    implicit def participationToJsonObject(participation: Participation): JsonObject =
      JsonHelper.toJsonObject(gson, participation)

  }

}
