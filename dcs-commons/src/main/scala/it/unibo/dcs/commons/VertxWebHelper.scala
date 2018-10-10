package it.unibo.dcs.commons

import io.vertx.scala.ext.web.RoutingContext

object VertxWebHelper {

  def getContextData(urlParameter: String)(implicit context: RoutingContext): Option[String] =
    context.request.getParam(urlParameter)

  def respondWithCode(statusCode: Int)(implicit context: RoutingContext): Unit =
    context.response.setStatusCode(statusCode).end

  def isCodeSuccessful(statusCode: Int): Boolean = statusCode / 100 == 2
}
