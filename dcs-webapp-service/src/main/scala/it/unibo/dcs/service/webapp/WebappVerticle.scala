package it.unibo.dcs.service.webapp

import io.vertx.lang.scala.ScalaVerticle._
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import it.unibo.dcs.commons.service.ServiceVerticle

final class WebappVerticle extends ServiceVerticle {

  override protected def initializeRouter(router: Router): Unit = {
    // Example of route
    // router.get("/...").handler(_.response().end())
  }

  override def start(): Unit = {
    startHttpServer("localhost", 8080) subscribe
  }
}


object Main extends App {
  Vertx vertx() deployVerticle nameForVerticle[WebappVerticle]
}

