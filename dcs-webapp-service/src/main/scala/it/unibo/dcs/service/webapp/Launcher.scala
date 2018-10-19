package it.unibo.dcs.service.webapp

import java.net.InetAddress

import io.vertx.lang.scala.ScalaLogger
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.{DeploymentOptions, Vertx, VertxOptions}
import it.unibo.dcs.commons.VertxHelper
import it.unibo.dcs.commons.VertxHelper.Implicits._
import it.unibo.dcs.service.webapp.verticles.WebAppVerticle

import scala.language.implicitConversions

/** Entry point of the application.
  * It launches the verticle associated with WebAppService in clustered mode. */
object Launcher extends App {

  private val logger = ScalaLogger.getLogger(getClass.getName)

<<<<<<< HEAD
  val port = args(0).toInt

  val config = new JsonObject()
      .put("host", InetAddress.getLocalHost.getHostAddress)
      .put("port", args(0).toInt)

  VertxHelper.toObservable[Vertx](Vertx.clusteredVertx(VertxOptions(), _))
    .subscribe(vertx => vertx deployVerticle(new WebAppVerticle, DeploymentOptions()
      .setConfig(config)),
=======
  val options = DeploymentOptions()
      .setConfig(new JsonObject()
        .put("host", InetAddress.getLocalHost.getHostAddress)
        .put("port", args(0).toInt))

  VertxHelper.toObservable[Vertx](Vertx.clusteredVertx(VertxOptions(), _))
    .subscribe(vertx => vertx deployVerticle(new WebAppVerticle, options),
>>>>>>> 6eaf76ce0b16e4dc75bd8070c0d83ea888c99b85
      cause => logger.error("", cause))

}
