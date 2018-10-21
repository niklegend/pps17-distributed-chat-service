package it.unibo.dcs.service.webapp.repositories.datastores.api.impl

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.scala.ext.web.client.WebClient
import it.unibo.dcs.commons.VertxWebHelper.responseStatus
import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.exceptions.{RegistrationResponseException, RoomCreationResponseException, RoomDeletionResponseException, RoomServiceErrorException}
import it.unibo.dcs.service.webapp.interaction.Requests.Implicits._
import it.unibo.dcs.service.webapp.interaction.Requests._
import it.unibo.dcs.service.webapp.model.Room
import it.unibo.dcs.service.webapp.repositories.datastores.api.RoomApi
import rx.lang.scala.Observable

import scala.concurrent.ExecutionContext.Implicits.global

class RoomRestApi(private[this] val discovery: HttpEndpointDiscovery)
  extends AbstractApi(discovery, "room-service") with RoomApi {

  override def createRoom(createRoomRequest: CreateRoomRequest): Observable[Room] = {
    for {
      response <- request((roomWebClient: WebClient) =>
        Observable.from(roomWebClient.post(RoomRestApi.createRoomURI).sendJsonObjectFuture(createRoomRequest)))
    } yield response.bodyAsJsonObject().getOrElse(throw RoomCreationResponseException("Room service returned an empty body"))
  }

  override def deleteRoom(deletionRequest: DeleteRoomRequest): Observable[Unit] = {
    for {
      response <- request((roomWebClient: WebClient) =>
        Observable.from(roomWebClient.post(RoomRestApi.deleteRoomURI).sendJsonObjectFuture(deletionRequest)))
    } yield response.bodyAsJsonObject().orElse(throw RoomDeletionResponseException("Room service returned an empty body"))
  }

  override def registerUser(userRegistrationRequest: RegisterUserRequest, token: String): Observable[Unit] = {
    for {
      response <- request((roomWebClient: WebClient) =>
        Observable.from(roomWebClient.post(RoomRestApi.registerUser).sendJsonObjectFuture(userRegistrationRequest)))
    } yield responseStatus(response) match {
      case HttpResponseStatus.OK => response.bodyAsJsonObject()
        .orElse(throw RegistrationResponseException("Room service returned an empty body",
          userRegistrationRequest.username, token))
      case _ =>
        val responseJson = response.bodyAsJsonObject()
          .getOrElse(throw RegistrationResponseException("Room service returned an empty body after an error",
            userRegistrationRequest.username, token))
        throw RoomServiceErrorException(responseJson, userRegistrationRequest.username, token)
    }
  }
}

private[impl] object RoomRestApi {

  val createRoomURI = "/createRoom"

  val deleteRoomURI = "/deleteRoom"

  val registerUser = "/registerUser"

}


