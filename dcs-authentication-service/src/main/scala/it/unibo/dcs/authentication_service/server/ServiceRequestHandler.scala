package it.unibo.dcs.authentication_service.server

import io.vertx.scala.ext.web.RoutingContext

trait ServiceRequestHandler {

<<<<<<< HEAD
  def handleRegistration(implicit context: RoutingContext): Unit

  def handleLogin(implicit context: RoutingContext): Unit

  def handleLogout(implicit context: RoutingContext): Unit
=======
  def handleRegistration(context: RoutingContext): Unit

  def handleLogin(context: RoutingContext): Unit

  def handleLogout(context: RoutingContext): Unit
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
}
