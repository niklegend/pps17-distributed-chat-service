package it.unibo.dcs.commons

import io.vertx.core.http.HttpMethod.{DELETE, POST}
import io.vertx.core.json.JsonObject
import io.vertx.scala.ext.web.RoutingContext

object VertxWebHelper {

  def getContextData(urlParameter: String)(implicit context: RoutingContext): Option[String] =
    context.request.getParam(urlParameter)

  def getJsonBodyData(fieldName: String)(implicit context: RoutingContext): Option[String] =
    context.getBody().map(body => body.toJsonObject.getString(fieldName))

  def respondWithCode(statusCode: Int)(implicit context: RoutingContext): Unit =
    context.response.setStatusCode(statusCode).end

  def respondOk(implicit context: RoutingContext): Unit = {
    var response = context.response
    context.request.method match {
      case POST => response = response.setStatusCode(204).putHeader("Location", context.request.absoluteURI)
      case DELETE => response = response.setStatusCode(204)
      case _ => response.setStatusCode(200)
    }
    response.end
  }

  def respondOkWithJson(body: JsonObject)(implicit context: RoutingContext): Unit = {
    context.response
      .putHeader("content-type", "application/json")
      .setStatusCode(200)
      .end(body.encodePrettily())
  }

  def isCodeSuccessful(statusCode: Int): Boolean = statusCode / 100 == 2

  def doIfDefined(id: Option[_], action: => Unit)(implicit context: RoutingContext): Unit =
    if (id.isDefined) {
      action
    } else respondWithCode(400)
}
