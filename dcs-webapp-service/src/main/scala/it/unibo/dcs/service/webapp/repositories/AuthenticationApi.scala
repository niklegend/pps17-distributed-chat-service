package it.unibo.dcs.service.webapp.repositories

import it.unibo.dcs.commons.service.{AbstractApi, HttpEndpointDiscovery}

class AuthenticationApi(private[this] val discovery: HttpEndpointDiscovery)
  extends AbstractApi(discovery, "AuthenticationService") {

}
