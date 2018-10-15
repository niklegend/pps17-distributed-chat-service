package it.unibo.dcs.service.webapp.repositories.datastores.api.impl

import io.vertx.lang.scala.json.Json
import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.service.webapp.interaction.Requests
import it.unibo.dcs.service.webapp.interaction.Requests.Implicits._
import it.unibo.dcs.service.webapp.interaction.Requests._
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import it.unibo.dcs.service.webapp.repositories.datastores.api.exceptions.{LoginResponseException, RegistrationResponseException, RoomCreationException}
import it.unibo.dcs.service.webapp.repositories.datastores.api.impl.AuthenticationRestApi._
<<<<<<< HEAD
=======
import rx.lang.scala.Observable
>>>>>>> 2ea58c7f4036bf5b775463df0c4e40ea2a9db0e3
import it.unibo.dcs.service.webapp.repositories.datastores.api.routes.protectedRoomURI
import rx.lang.scala.Observable

import scala.concurrent.ExecutionContext.Implicits.global

class AuthenticationRestApi(private[this] val discovery: HttpEndpointDiscovery)
  extends AbstractApi(discovery, "authentication-service") with AuthenticationApi {

  /* JWT Token labels */
  private val authenticationKeyLabel = "Authorization"
  private val tokenPrefix = "Bearer "


  override def loginUser(loginUserRequest: LoginUserRequest): Observable[String] = {
    request(authWebClient =>
      Observable.from(authWebClient.post(loginUserURI).sendJsonObjectFuture(loginUserRequest)))
      .map(response => response.bodyAsString().getOrElse(throw LoginResponseException()))
  }


  override def registerUser(registerRequest: RegisterUserRequest): Observable[String] = {
    request(authWebClient =>
      Observable.from(authWebClient.post(registerUserURI).sendJsonObjectFuture(registerRequest)))
      .map(response => response.bodyAsString().getOrElse(throw RegistrationResponseException()))
  }


  override def logoutUser(logoutRequest: LogoutUserRequest): Observable[Unit] = {
    request(authWebClient =>
      Observable.from(authWebClient.post(logoutUserURI)
        .putHeader(authenticationKeyLabel, tokenPrefix + logoutRequest.token)
        .sendJsonObjectFuture(logoutRequest)))
      .map(_.body())
  }


  override def createRoom(roomCreationRequest: CreateRoomRequest): Observable[Unit] =
    request(roomWebClient =>
      Observable.from(roomWebClient.post(protectedRoomURI)
        .putHeader(authenticationKeyLabel, tokenPrefix + roomCreationRequest.token)
        .sendJsonObjectFuture(roomCreationRequest)))
      .map(response => response.bodyAsString().getOrElse(throw RoomCreationException()))

  override def checkToken(checkRoomRequest: CheckTokenRequest): Observable[Unit] =
    request(tokenWebClient =>
    Observable.from(tokenWebClient.get(checkTokenURI)
      .putHeader(authenticationKeyLabel, tokenPrefix + checkRoomRequest.token)
      .sendJsonObjectFuture(Json.obj())))
    .map(_.body())
}

private[impl] object AuthenticationRestApi {

  val loginUserURI = "/login"
  val registerUserURI = "/register"
  val logoutUserURI = "/protected/logout"
  val checkTokenURI = "/protected/tokenValidity"

}
