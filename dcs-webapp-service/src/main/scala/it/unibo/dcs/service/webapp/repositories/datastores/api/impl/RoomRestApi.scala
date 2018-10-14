package it.unibo.dcs.service.webapp.repositories.datastores.api.impl

import io.vertx.scala.ext.web.client.WebClient
import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.service.webapp.repositories.Requests.DeleteRoomRequest
import it.unibo.dcs.service.webapp.repositories.datastores.api.RoomApi
import rx.lang.scala.Observable
import it.unibo.dcs.service.webapp.repositories.Requests.Implicits._
import it.unibo.dcs.service.webapp.repositories.datastores.api.exceptions.RoomDeletionResponseException
import scala.concurrent.ExecutionContext.Implicits.global

class RoomRestApi(private[this] val discovery: HttpEndpointDiscovery)
  extends AbstractApi(discovery, "RoomService") with RoomApi {

  override def deleteRoom(deletionRequest: DeleteRoomRequest): Observable[Unit] = {
    request((webClient: WebClient) =>
      Observable.from(webClient.post("/deleteRoom").sendJsonObjectFuture(deletionRequest)))
      .map(response => response.bodyAsJsonObject()
        .getOrElse(throw RoomDeletionResponseException())
      )
  }

  override def createRoom(createRoomRequest: CreateRoomRequest): Observable[Room] = {
    for {
      response <- request((roomWebClient: WebClient) =>
        Observable.from(roomWebClient.post(createRoomURI).sendJsonObjectFuture(createRoomRequest)))
    } yield response.bodyAsJsonObject().getOrElse(throw RoomCreationException())
  }
}

private[impl] object RoomRestApi {

  val createRoomURI = "/createRoom"

}
