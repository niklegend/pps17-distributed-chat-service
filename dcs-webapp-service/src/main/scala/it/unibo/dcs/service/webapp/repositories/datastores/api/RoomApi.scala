package it.unibo.dcs.service.webapp.repositories.datastores.api

import io.vertx.core.Vertx
import io.vertx.scala.core.eventbus.EventBus
import io.vertx.servicediscovery.ServiceDiscovery
import it.unibo.dcs.commons.service.HttpEndpointDiscoveryImpl
import it.unibo.dcs.service.webapp.model.{Room, User}
import it.unibo.dcs.service.webapp.repositories.datastores.api.impl.UserRestApi
import rx.lang.scala.Observable

trait RoomApi {
  def createRoom(name: String, creator: User): Observable[Room]
}

object RoomApi {
  def roomRestApi(vertx: Vertx, eventBus: EventBus): RoomApi =
    new RoomRestApi(
      new HttpEndpointDiscoveryImpl(ServiceDiscovery.create(vertx), eventBus))
}
