package it.unibo.dcs.service.webapp

import io.vertx.lang.scala.ScalaVerticle._
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.BodyHandler
import it.unibo.dcs.commons.service.ServiceVerticle

final class WebappVerticle extends ServiceVerticle {

  override protected def initializeRouter(router: Router): Unit = {
    // Example of route
    // router.get("/...").handler(_.response().end())
    router.route().handler(BodyHandler.create())
    val requestHandler: ServiceRequestHandler = new ServiceRequestHandlerImpl()
    router.post("/register").handler(requestHandler.handleRegistration(_))
    router.post("/login").handler(requestHandler.handleLogin(_))
    router.post("/protected/logout").handler(requestHandler.handleLogout(_))
  }

  override def start(): Unit = {
    startHttpServer("localhost", 8080) subscribe()
  }
}


object Main extends App {
  Vertx vertx() deployVerticle nameForVerticle[WebappVerticle]
}

