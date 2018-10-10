package it.unibo.dcs.service.webapp.repositories.datastores.api.impl

import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.service.webapp.interaction.Requests.Implicits._
import it.unibo.dcs.service.webapp.interaction.Requests.{CreateRoomRequest, LoginUserRequest, RegisterUserRequest}
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import it.unibo.dcs.service.webapp.repositories.datastores.api.exceptions.{RoomCreationException, LoginResponseException, RegistrationResponseException}
import rx.lang.scala.Observable

import scala.concurrent.ExecutionContext.Implicits.global


class AuthenticationRestApi(private[this] val discovery: HttpEndpointDiscovery)
  extends AbstractApi(discovery, "AuthenticationService") with AuthenticationApi {

  override def loginUser(loginUserRequest: LoginUserRequest): Observable[String] = {
    request(authWebClient =>
      Observable.from(authWebClient.post("/login").sendJsonObjectFuture(loginUserRequest)))
      .map(response => response.bodyAsString().getOrElse(throw LoginResponseException()))
  }

  override def registerUser(registerRequest: RegisterUserRequest): Observable[String] = {
    request(authWebClient =>
      Observable.from(authWebClient.post("/register").sendJsonObjectFuture(registerRequest)))
      .map(response => response.bodyAsString().getOrElse(throw RegistrationResponseException()))
  }

  override def logoutUser(username: String): Observable[Unit] = {
    request(authWebClient =>
      Observable.from(authWebClient.post("/protected/logout").sendJsonObjectFuture(username)))
      .map(_.body())
  }

  override def createRoom(roomCreationRequest: CreateRoomRequest): Observable[String] =
    request(roomWebClient =>
      Observable.from(roomWebClient.post("/register").sendJsonObjectFuture(roomCreationRequest)))
      .map(response => response.bodyAsString().getOrElse(throw RoomCreationException()))
}
