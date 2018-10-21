package it.unibo.dcs.service.webapp.repositories.datastores.api.impl

import io.vertx.lang.scala.json.{Json, JsonObject}
import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.exceptions.{InternalException, bodyAsJsonObject}
import it.unibo.dcs.service.webapp.interaction.Requests.Implicits._
import it.unibo.dcs.service.webapp.interaction.Requests._
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import it.unibo.dcs.service.webapp.repositories.datastores.api.impl.AuthenticationRestApi._
import rx.lang.scala.Observable

import scala.concurrent.ExecutionContext.Implicits.global

class AuthenticationRestApi(private[this] val discovery: HttpEndpointDiscovery)
  extends AbstractApi(discovery, "authentication-service") with AuthenticationApi {

  /* JWT Token labels */
  private val authenticationKeyLabel = "Authorization"
  private val tokenPrefix = "Bearer "

  override def loginUser(loginUserRequest: LoginUserRequest): Observable[String] = request(authWebClient =>
    Observable.from(authWebClient.post(loginUserURI).sendJsonObjectFuture(loginUserRequest)))
    .map(bodyAsJsonObject(throw InternalException("Authentication service returned an empty body")))
    .map(getToken)

  override def registerUser(registerRequest: RegisterUserRequest): Observable[String] = {
    request(authWebClient =>
      Observable.from(authWebClient.post(registerUserURI).sendJsonObjectFuture(registerRequest)))
      .map(bodyAsJsonObject(throw InternalException("Authentication service returned an empty body")))
      .map(getToken)
  }

  override def logoutUser(logoutRequest: LogoutUserRequest): Observable[Unit] = {
    request(authWebClient =>
      Observable.from(authWebClient.post(logoutUserURI)
        .putHeader(authenticationKeyLabel, tokenPrefix + logoutRequest.token)
        .sendJsonObjectFuture(logoutRequest)))
      .map(bodyAsJsonObject(Json.emptyObj()))
      .map(_ => ())
  }

  override def createRoom(roomCreationRequest: CreateRoomRequest): Observable[Unit] =
    checkToken(CheckTokenRequest(roomCreationRequest.token))

  override def checkToken(checkRoomRequest: CheckTokenRequest): Observable[Unit] =
    request(tokenWebClient =>
      Observable.from(tokenWebClient.get(checkTokenURI)
        .putHeader(authenticationKeyLabel, tokenPrefix + checkRoomRequest.token)
        .sendJsonObjectFuture(Json.obj())))
      .map(bodyAsJsonObject())
    .map(_ => ())

  override def checkLogout(logoutUserRequest: LogoutUserRequest): Observable[Unit] = {
    request(authWebClient =>
      Observable.from(authWebClient.post(checkLogoutURI).sendJsonObjectFuture(logoutUserRequest)))
      .map(bodyAsJsonObject())
      .map(_ => ())
  }

}

private[impl] object AuthenticationRestApi {

  val loginUserURI = "/login"
  val registerUserURI = "/register"
  val checkLogoutURI = "/validateLogout"
  val logoutUserURI = "/protected/logout"
  val checkTokenURI = "/protected/tokenValidity"

  private[impl] def getToken(json: JsonObject): String = {
    json.getString("token")
  }

}
