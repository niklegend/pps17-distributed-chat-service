package it.unibo.dcs.authentication_service.server
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.commons.VertxWebHelper

class ServiceRequestHandlerImpl extends ServiceRequestHandler {

  override def handleRegistration(context: RoutingContext): Unit = {
    VertxWebHelper
  }

  override def handleLogin(context: RoutingContext): Unit = ???

  override def handleLogout(context: RoutingContext): Unit = ???
}
