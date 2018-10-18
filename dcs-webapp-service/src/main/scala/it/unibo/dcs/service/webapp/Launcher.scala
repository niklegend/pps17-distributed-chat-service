package it.unibo.dcs.service.webapp

import java.net.InetAddress

import io.vertx.lang.scala.ScalaLogger
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.{DeploymentOptions, Vertx, VertxOptions}
import it.unibo.dcs.commons.VertxHelper
import it.unibo.dcs.commons.VertxHelper.Implicits._
import it.unibo.dcs.service.webapp.verticles.WebAppVerticle
import it.unibo.dcs.service.webapp.verticles.utils.DeploymentUtils.deploymentOptions

import scala.language.implicitConversions

/** Entry point of the application.
  * It launches the verticle associated with WebAppService in clustered mode. */
object Launcher extends App {

  private val logger = ScalaLogger.getLogger(getClass.getName)

  val options = DeploymentOptions()
      .setConfig(new JsonObject()
        .put("host", InetAddress.getLocalHost.getHostAddress)
        .put("port", args(0).toInt))

  VertxHelper.toObservable[Vertx](Vertx.clusteredVertx(VertxOptions(), _))
    .subscribe(vertx => vertx deployVerticle(new WebAppVerticle, options),
      cause => logger.error("", cause))

}
