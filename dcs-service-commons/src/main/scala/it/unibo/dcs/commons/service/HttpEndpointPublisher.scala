package it.unibo.dcs.commons.service

import io.vertx.core.json.JsonObject
import io.vertx.servicediscovery.Record
import it.unibo.dcs.commons.service.HttpEndpointPublisher._

import scala.concurrent.{ExecutionContext, Future}

trait HttpEndpointPublisher {

  def publish(name: String,
              ssl: Boolean = DEFAULT_SSL,
              host: String = DEFAULT_HOST,
              port: Int = DEFAULT_PORT,
              root: String = DEFAULT_ROOT,
              metadata: JsonObject = DEFAULT_METADATA): Future[Record]

  def unpublish(record: Record): Future[Record]

  def clear()(implicit executor: ExecutionContext): Future[Unit]

}

object HttpEndpointPublisher {

  val DEFAULT_SSL = false
  val DEFAULT_HOST = "0.0.0.0"
  val DEFAULT_PORT = 80
  val DEFAULT_ROOT = "/"

  def DEFAULT_METADATA = new JsonObject()

}
