package it.unibo.dcs.service.room

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.lang.scala.json.{Json, JsonArray, JsonObject}
import io.vertx.scala.core.http.HttpServerResponse
import it.unibo.dcs.commons.JsonHelper.Implicits.{RichGson, jsonArrayToString, jsonObjectToString}
import it.unibo.dcs.commons.Logging
import it.unibo.dcs.commons.VertxWebHelper.Implicits._
import it.unibo.dcs.exceptions.ErrorSubscriber
import it.unibo.dcs.service.room.model.{Participation, Room}
import it.unibo.dcs.service.room.subscriber.Implicits._
import rx.lang.scala.Subscriber

import scala.language.implicitConversions

package object subscriber {

  final class CreateRoomSubscriber(protected override val response: HttpServerResponse) extends Subscriber[Room]
    with ErrorSubscriber with Logging {

    override def onNext(room: Room): Unit = {
      val json: JsonObject = room
      log.info(s"Answering with room: $json")
      response.setStatus(HttpResponseStatus.CREATED).end(json)
    }

  }

  final class JoinRoomSubscriber(protected override val response: HttpServerResponse) extends Subscriber[Participation]
    with ErrorSubscriber with Logging {

    override def onNext(participation: Participation): Unit = {
      val json: JsonObject = participation
      log.info(s"Answering with participation: $json")
      response.setStatus(HttpResponseStatus.CREATED).end(json)
    }
  }

  final class CreateUserSubscriber(protected override val response: HttpServerResponse) extends Subscriber[Unit]
    with ErrorSubscriber with Logging {

    override def onCompleted(): Unit = response.setStatus(HttpResponseStatus.CREATED).end()

  }

  class DeleteRoomSubscriber(protected override val response: HttpServerResponse) extends Subscriber[String]
    with ErrorSubscriber {

    override def onNext(name: String): Unit =
      response.end(Json.obj(("name", name)))

  }

  class GetRoomsSubscriber(protected override val response: HttpServerResponse) extends Subscriber[List[Room]]
    with ErrorSubscriber {

    override def onNext(rooms: List[Room]): Unit = {
      val res: JsonArray = rooms
      response.end(res)
    }

  }

  class GetUserParticipationsSubscriber(protected override val response: HttpServerResponse) extends Subscriber[List[Room]]
    with ErrorSubscriber {

    override def onNext(rooms: List[Room]): Unit = {
      val res: JsonArray = rooms
      response.end(res)
    }

  }

  object Implicits {

    implicit def roomToJsonObject(room: Room): JsonObject = gson toJsonObject room

    implicit def roomsToJsonObject(rooms: Iterable[Room]): JsonArray = gson toJsonArray rooms

    implicit def participationToJsonObject(participation: Participation): JsonObject = gson toJsonObject participation

  }

}
