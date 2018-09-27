package it.unibo.dcs.commons.service

import io.vertx.core.json.JsonObject
import io.vertx.scala.core.http.HttpServerOptions
import io.vertx.scala.ext.web.client.WebClient

import scala.concurrent.{ExecutionContext, Future}

trait HttpEndpointDiscovery {

  def getWebClient(name: String)(implicit executor: ExecutionContext): Future[WebClient]

  def getWebClientOrFail(name: String)(implicit executor: ExecutionContext): Future[WebClient]

}
