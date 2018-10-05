package it.unibo.dcs.service.webapp

import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.service.webapp.usecases.RegisterUserUseCase

class ServiceRequestHandlerImpl extends ServiceRequestHandler {
  override def handleRegistration(context: RoutingContext): Unit = {
    val registrationInfo = context.getBodyAsJson()
  }

  override def handleLogout(context: RoutingContext): Unit = ???

  override def handleLogin(context: RoutingContext): Unit = ???
}
