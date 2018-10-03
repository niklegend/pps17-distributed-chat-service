package it.unibo.dcs.service.webapp.repositories.datastores.api.impl

import io.vertx.lang.scala.json.JsonObject
import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.service.webapp.repositories.Requests
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import rx.lang.scala.Observable

import scala.concurrent.ExecutionContext.Implicits.global


class AuthenticationRestApi(private[this] val discovery: HttpEndpointDiscovery,
                            val serviceName: String = "AuthenticationService")
  extends AbstractApi(discovery, serviceName) with AuthenticationApi {

  override def loginUser(username: String, password: String): Observable[String] = {
    val loginJson: JsonObject = createLoginJson(username, password)
    request(authWebClient =>
      Observable.from(authWebClient.post("/login").sendJsonObjectFuture(loginJson)))
      .map(response => response.bodyAsString().get)
  }

  private def createLoginJson(username: String, password: String): JsonObject = {
    val loginJson = new JsonObject
    loginJson.put("username", username)
    loginJson.put("password", password)
  }

  override def registerUser(registerRequest: Requests.RegisterUserRequest): Observable[String] = {
    val registrationJson: JsonObject = createRegistrationJson(registerRequest)
    request(authWebClient =>
      Observable.from(authWebClient.post("/register").sendJsonObjectFuture(registrationJson)))
      .map(response => response.bodyAsString().get)
  }

  private def createRegistrationJson(registerRequest: Requests.RegisterUserRequest) = {
    val registrationJson = new JsonObject
    registrationJson.put("username", registerRequest.firstName)
    registrationJson.put("lastname", registerRequest.lastName)
    registrationJson.put("password", registerRequest.password)
    registrationJson.put("username", registerRequest.username)
  }

  override def logoutUser(username: String): Observable[Unit] = {
    val logoutJson: JsonObject = createLogoutJson(username)
    request(authWebClient =>
      Observable.from(authWebClient.post("/protected/logout").sendJsonObjectFuture(logoutJson)))
      .map(_.body())
  }

  private def createLogoutJson(username: String): JsonObject = {
    val logoutJson = new JsonObject
    logoutJson.put("username", username)
  }
}
