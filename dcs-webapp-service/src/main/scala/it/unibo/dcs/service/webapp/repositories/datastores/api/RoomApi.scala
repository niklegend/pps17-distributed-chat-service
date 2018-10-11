package it.unibo.dcs.service.webapp.repositories.datastores.api

import io.vertx.core.Vertx
import io.vertx.scala.core.eventbus.EventBus
import io.vertx.servicediscovery.ServiceDiscovery
import it.unibo.dcs.commons.service.HttpEndpointDiscoveryImpl
import it.unibo.dcs.service.webapp.repositories.Requests.DeleteRoomRequest
import it.unibo.dcs.service.webapp.repositories.datastores.api.impl.RoomRestApi
import rx.lang.scala.Observable

trait RoomApi {

  def deleteRoom(request: DeleteRoomRequest): Observable[Unit]
}

/* Companion object */
object RoomApi {
  /* Factory method */
  def roomRestApi(vertx: Vertx, eventBus: EventBus): RoomApi = new RoomRestApi(
    new HttpEndpointDiscoveryImpl(ServiceDiscovery.create(vertx), eventBus))
}