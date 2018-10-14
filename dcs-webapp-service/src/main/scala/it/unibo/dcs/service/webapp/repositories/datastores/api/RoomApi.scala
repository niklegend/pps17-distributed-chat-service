package it.unibo.dcs.service.webapp.repositories.datastores.api

import io.vertx.core.Vertx
import io.vertx.scala.core.eventbus.EventBus
import io.vertx.servicediscovery.ServiceDiscovery
import it.unibo.dcs.commons.service.HttpEndpointDiscoveryImpl
import it.unibo.dcs.service.webapp.interaction.Requests.CreateRoomRequest
import it.unibo.dcs.service.webapp.model.Room
import it.unibo.dcs.service.webapp.repositories.datastores.api.impl.RoomRestApi
import rx.lang.scala.Observable

/** Utility wrapper for making requests to the Room Service via the network */
trait RoomApi {

  /** It tells the Room Service to store a new room
    *
    * @param request room to create information
    * @return an observable stream of just the created room */
  def createRoom(request: CreateRoomRequest): Observable[Room]
}

/** Companion object */
object RoomApi {

  /** Factory method to create a rest api that communicates with the Room Service
    *
    * @param vertx    Vertx instance
    * @param eventBus Vertx Event Bus
    * @return the RoomRestApi instance */
  def roomRestApi(vertx: Vertx, eventBus: EventBus): RoomApi =
    new RoomRestApi(new HttpEndpointDiscoveryImpl(ServiceDiscovery.create(vertx), eventBus))
}
