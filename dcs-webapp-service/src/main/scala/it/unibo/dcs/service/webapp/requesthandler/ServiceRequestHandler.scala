package it.unibo.dcs.service.webapp.requesthandler

import io.vertx.scala.ext.web.RoutingContext

trait ServiceRequestHandler {

  def handleLogin(context: RoutingContext): Unit

  def handleRegistration(context: RoutingContext): Unit

  def handleLogout(context: RoutingContext): Unit

}
