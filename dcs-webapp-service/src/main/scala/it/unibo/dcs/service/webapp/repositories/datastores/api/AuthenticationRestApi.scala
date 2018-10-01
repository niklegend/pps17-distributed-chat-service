package it.unibo.dcs.service.webapp.repositories.datastores.api

import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import it.unibo.dcs.service.webapp.repositories.Requests
import rx.Single

class AuthenticationRestApi(private[this] val discovery: HttpEndpointDiscovery, val serviceName: String = "AuthenticationService")
  extends AbstractApi(discovery, serviceName) with AuthenticationApi {

  override def loginUser(username: String, password: String): Single[Boolean] = ???

  override def registerUser(request: Requests.RegisterUserRequest): Single[Boolean] = {
    if (clientOption.isEmpty) return Single just false
    val authServiceClient = clientOption.get
    //Single.from(authServiceClient.post("localhost", "/user/register").sendJsonObjectFuture(new JsonObject()))
    Single.just(true)
  }

  override def logoutUser(username: String): Single[Boolean] = ???

}
