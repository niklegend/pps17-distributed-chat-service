package it.unibo.dcs.service.webapp.repositories

import io.vertx.scala.ext.web.client.WebClient
import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}
import rx.Single

class AuthenticationRestApi(private[this] val discovery: HttpEndpointDiscovery, var authWebClient: WebClient,
                            val serviceName: String = "AuthenticationService")
  extends AbstractApi(discovery, serviceName) with AuthenticationApi {

  override def loginUser(username: String, password: String): Single[Boolean] = ???

  override def registerUser(request: Requests.RegisterUserRequest): Single[Boolean] = ???

  override def logoutUser(username: String): Single[Boolean] = ???

  def discoverAuthWebClient() = discovery.getWebClient(serviceName)
    .subscribe(authWebClient => this.authWebClient = authWebClient)
}
