package it.unibo.dcs.service.webapp.repositories.datastores.api.impl

import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.service.webapp.repositories.Requests
import it.unibo.dcs.service.webapp.repositories.Requests.Implicits._
import it.unibo.dcs.service.webapp.repositories.Requests.LoginUserRequest
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import rx.lang.scala.Observable

import scala.concurrent.ExecutionContext.Implicits.global


class AuthenticationRestApi(private[this] val discovery: HttpEndpointDiscovery)
  extends AbstractApi(discovery, "AuthenticationService") with AuthenticationApi {

  override def loginUser(loginUserRequest: LoginUserRequest): Observable[String] = {
    request(authWebClient =>
      Observable.from(authWebClient.post("/login").sendJsonObjectFuture(loginUserRequest)))
      .map(response => response.bodyAsString().getOrElse(throw new IllegalArgumentException(response.statusMessage())))
  }

  override def registerUser(registerRequest: Requests.RegisterUserRequest): Observable[String] = {
    request(authWebClient =>
      Observable.from(authWebClient.post("/register").sendJsonObjectFuture(registerRequest)))
      .map(response => response.bodyAsString().getOrElse(throw new IllegalArgumentException(response.statusMessage())))
  }

  override def logoutUser(username: String): Observable[Unit] = {
    request(authWebClient =>
      Observable.from(authWebClient.post("/protected/logout").sendJsonObjectFuture(username)))
      .map(_.body())
  }


}
