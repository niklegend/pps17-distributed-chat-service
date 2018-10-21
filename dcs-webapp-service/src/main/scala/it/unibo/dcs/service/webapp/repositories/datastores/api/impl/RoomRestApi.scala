package it.unibo.dcs.service.webapp.repositories.datastores.api.impl

import io.vertx.lang.scala.json.JsonObject
import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.exceptions.{InternalException, bodyAsJsonObject}
import it.unibo.dcs.service.webapp.interaction.Requests
import it.unibo.dcs.service.webapp.interaction.Requests.Implicits._
import it.unibo.dcs.service.webapp.interaction.Requests.{CreateRoomRequest, DeleteRoomRequest}
import it.unibo.dcs.service.webapp.model.Room
import it.unibo.dcs.service.webapp.repositories.datastores.api.RoomApi
import it.unibo.dcs.service.webapp.repositories.datastores.api.impl.RoomRestApi.getRoom
import rx.lang.scala.Observable

import scala.concurrent.ExecutionContext.Implicits.global

class RoomRestApi(private[this] val discovery: HttpEndpointDiscovery)
  extends AbstractApi(discovery, "room-service") with RoomApi {

  override def createRoom(createRoomRequest: CreateRoomRequest): Observable[Room] = {
    request(client =>
      Observable.from(client.post(RoomRestApi.createRoomURI).sendJsonObjectFuture(createRoomRequest)))
        .map(bodyAsJsonObject(throw InternalException("Room service returned an empty body")))
        .map(getRoom)
  }

  override def deleteRoom(deletionRequest: DeleteRoomRequest): Observable[String] = {
    request(client =>
      Observable.from(client.post(RoomRestApi.deleteRoomURI).sendJsonObjectFuture(deletionRequest)))
      .map(bodyAsJsonObject(throw InternalException("Room service returned an empty body")))
      .map(_.getString("name"))
  }

  override def registerUser(userRegistrationRequest: Requests.RegisterUserRequest): Observable[Unit] = {
    request(client =>
        Observable.from(client.post(RoomRestApi.createUser).sendJsonObjectFuture(userRegistrationRequest)))
      .map(bodyAsJsonObject())
      .map(_ => {})
  }
}

private[impl] object RoomRestApi {

  val createRoomURI = "/createRoom"

  val deleteRoomURI = "/deleteRoom"

  val createUser = "/createUser"

  private[impl] def getRoom(json: JsonObject) = {
    Room(json.getString("name"))
  }

}
