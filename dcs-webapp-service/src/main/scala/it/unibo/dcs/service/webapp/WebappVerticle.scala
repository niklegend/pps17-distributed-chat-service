package it.unibo.dcs.service.webapp

import io.vertx.scala.ext.web.Router
import io.vertx.servicediscovery.ServiceDiscovery
import it.unibo.dcs.commons.service.{HttpEndpointDiscovery, HttpEndpointDiscoveryImpl, ServiceVerticle}
import it.unibo.dcs.service.webapp.repositories.AuthenticationRestApi

final class WebappVerticle extends ServiceVerticle {

  private def discovery: HttpEndpointDiscovery = new HttpEndpointDiscoveryImpl(ServiceDiscovery.create(), eventBus = eventBus)

  override protected def initializeRouter(router: Router): Unit = ???

  override def start(): Unit = {
    startHttpServer("localhost", 8080).subscribe()
  }
}
