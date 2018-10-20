package it.unibo.dcs.service.webapp.repositories.datastores.api.impl

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.buffer.Buffer
import io.vertx.scala.ext.web.client.{HttpResponse, WebClient}
import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.exceptions.{GetUserResponseException, UserCreationResponseException, UserServiceErrorException}
import it.unibo.dcs.service.webapp.interaction.Requests.Implicits._
import it.unibo.dcs.service.webapp.interaction.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.datastores.api.UserApi
import it.unibo.dcs.service.webapp.repositories.datastores.api.impl.UserRestApi._
import rx.lang.scala.Observable

import scala.concurrent.ExecutionContext.Implicits.global

class UserRestApi(private[this] val discovery: HttpEndpointDiscovery)
  extends AbstractApi(discovery, "user-service") with UserApi {

  override def createUser(registrationRequest: RegisterUserRequest, token: String): Observable[User] = {
    request((userWebClient: WebClient) =>
      Observable.from(userWebClient.post(createUserURI).sendJsonObjectFuture(registrationRequest)))
      .map(response => responseStatus(response) match {
        case HttpResponseStatus.OK => responseAsUser(registrationRequest, token, response)
        case _ => throw UserServiceErrorException(responseAsUser(registrationRequest, token, response),
          registrationRequest.username, token)
      })
  }

  private def responseAsUser(registrationRequest: RegisterUserRequest, token: String, response: HttpResponse[Buffer]) = {
    response.bodyAsJsonObject()
      .getOrElse(throw UserCreationResponseException("User service returned an empty body",
        registrationRequest.username, token))
  }

  private def responseStatus(response: HttpResponse[Buffer]) = {
    HttpResponseStatus.valueOf(response.statusCode())
  }

  override def getUserByUsername(username: String): Observable[User] =
    request((userWebClient: WebClient) =>
      Observable.from(userWebClient.get(getUserURI(username)).sendFuture()))
      .map(response => response.bodyAsJsonObject()
        .getOrElse(throw GetUserResponseException("User service returned an empty body")))

}

private[impl] object UserRestApi {

  val createUserURI = "/createUser"

  val validateRegistration = "/validateRegistration"

  def getUserURI(username: String) = s"/getUser/$username"

}
