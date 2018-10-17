package it.unibo.dcs.service.webapp

import com.hazelcast.config.Config
import io.vertx.lang.scala.ScalaLogger
import io.vertx.scala.core.Vertx
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager
import it.unibo.dcs.commons.VertxHelper
import it.unibo.dcs.commons.VertxHelper.Implicits._
import it.unibo.dcs.service.webapp.verticles.WebAppVerticle
import it.unibo.dcs.service.webapp.verticles.utils.DeploymentUtils.deploymentOptions

import scala.language.implicitConversions

/** Entry point of the application.
  * It launches the verticle associated with WebAppService in clustered mode. */
object Launcher extends App {

  private val logger = ScalaLogger.getLogger(getClass.getName)

  private val hazelcastConfig = new Config()
  hazelcastConfig.getProperties.setProperty("hazelcast.rest.enabled", "true")
  private val manager = new HazelcastClusterManager(hazelcastConfig)
  private val options = io.vertx.scala.core.VertxOptions(new io.vertx.core.VertxOptions().setClusterManager(manager))

  VertxHelper.toObservable[Vertx](Vertx.clusteredVertx(options, _))
    .subscribe(vertx => vertx deployVerticle(new WebAppVerticle, deploymentOptions),
      cause => logger.error("", cause))
}
