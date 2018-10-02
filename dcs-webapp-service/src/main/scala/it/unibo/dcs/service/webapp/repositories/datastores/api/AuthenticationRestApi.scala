package it.unibo.dcs.service.webapp.repositories.datastores.api

import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.service.webapp.repositories.Requests
import rx.Single
import rx.lang.scala.Observable

class AuthenticationRestApi(private[this] val discovery: HttpEndpointDiscovery, val serviceName: String = "AuthenticationService")
  extends AbstractApi(discovery, serviceName) with AuthenticationApi {

  override def loginUser(username: String, password: String): Observable[String] = ???

  override def registerUser(request: Requests.RegisterUserRequest): Observable[String] = {
    //if (clientOption.isEmpty) return Observable just "token"
    //val authServiceClient = get
    //Single.from(authServiceClient.post("localhost", "/user/register").sendJsonObjectFuture(new JsonObject()))
    Observable.just("token")
  }

  override def logoutUser(username: String): Observable[Unit] = ???

}
