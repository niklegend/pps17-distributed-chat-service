package it.unibo.dcs.service.webapp.repositories.datastores.api

import io.vertx.core.Vertx._
import io.vertx.scala.core.Vertx
import io.vertx.servicediscovery.ServiceDiscovery
import it.unibo.dcs.commons.service.HttpEndpointDiscoveryImpl
import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.datastores.api.impl.UserVertxRestApi
import rx.lang.scala.Observable

trait UserApi {
  def createUser(request: RegisterUserRequest): Observable[User]

  def getUserByUsername(username: String): Observable[User]
}

object UserApi {
  def userVertxRestApi: UserApi = new UserVertxRestApi(
    new HttpEndpointDiscoveryImpl(ServiceDiscovery.create(vertx()), Vertx vertx() eventBus()))
}