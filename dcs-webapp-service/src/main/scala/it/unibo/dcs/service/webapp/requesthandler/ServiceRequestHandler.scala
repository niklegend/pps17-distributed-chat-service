package it.unibo.dcs.service.webapp.requesthandler

import io.vertx.scala.core.Context
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, UserRepository}
import it.unibo.dcs.service.webapp.requesthandler.impl.ServiceRequestHandlerImpl

trait ServiceRequestHandler {

  def handleLogin(ctx: Context, context: RoutingContext): Unit

  def handleRegistration(ctx: Context, context: RoutingContext): Unit

  def handleLogout(ctx: Context, context: RoutingContext): Unit

}

object ServiceRequestHandler {
  def apply(): ServiceRequestHandler = new ServiceRequestHandlerImpl(UserRepository(), AuthenticationRepository())
}