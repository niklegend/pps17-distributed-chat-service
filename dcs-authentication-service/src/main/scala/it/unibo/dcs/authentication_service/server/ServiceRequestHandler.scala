package it.unibo.dcs.authentication_service.server

import io.vertx.scala.ext.web.RoutingContext

trait ServiceRequestHandler {

  def handleRegistration(context: RoutingContext): Unit

  def handleLogin(context: RoutingContext): Unit

  def handleLogout(context: RoutingContext): Unit
}
