package it.unibo.dcs.service.webapp.repositories.datastores.api.impl

import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.service.webapp.model.{Room, User}
import it.unibo.dcs.service.webapp.repositories.datastores.api.RoomApi
import rx.lang.scala.Observable

class RoomRestApi(private[this] val discovery: HttpEndpointDiscovery)
  extends AbstractApi(discovery, "RoomService") with RoomApi {

  override def createRoom(name: String, creator: User): Observable[Room] = {
    /*request((roomWebClient: WebClient) =>
      Observable.from(roomWebClient.post("/api/room/").sendJsonObjectFuture(username)))
      .map(response => response.bodyAsJsonObject()
        .getOrElse(throw GetUserResponseException())
      )*/
    ???
  }
}
