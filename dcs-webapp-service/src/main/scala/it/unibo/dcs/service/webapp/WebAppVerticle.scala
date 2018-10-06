package it.unibo.dcs.service.webapp

import io.vertx.core.Vertx
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.BodyHandler
import io.vertx.servicediscovery.{Record, ServiceDiscovery}
import it.unibo.dcs.commons.service.codecs.RecordMessageCodec
import it.unibo.dcs.commons.service.{HttpEndpointPublisherImpl, ServiceVerticle}
import it.unibo.dcs.service.webapp.requesthandler.ServiceRequestHandler
import io.vertx.core.eventbus.{EventBus => JEventBus}


final class WebappVerticle extends ServiceVerticle {

  override protected def initializeRouter(router: Router): Unit = {
    // To fetch request bodies
    router.route().handler(BodyHandler.create())

    val requestHandler: ServiceRequestHandler = ServiceRequestHandler()

    router.post("/register")
      .consumes("application/json")
      .produces("application/json")
      .handler(context => requestHandler handleRegistration(this.ctx, context))


    router.post("/login")
      .consumes("application/json")
      .produces("application/json")
      .handler(context => requestHandler handleLogin(this.ctx, context))


    router.post("/protected/logout")
      .consumes("application/json")
      .handler(context => requestHandler handleLogout(this.ctx, context))
  }

  override def start(): Unit = {
    val host = "localhost"
    val port = "8080"
    val discovery = ServiceDiscovery.create(Vertx.vertx())
    val eventBus = vertx.eventBus
    eventBus.asJava.asInstanceOf[JEventBus].registerDefaultCodec(classOf[Record], new RecordMessageCodec())
    val publisher = new HttpEndpointPublisherImpl(discovery, eventBus)
    startHttpServer("localhost", 8080)
      .doOnCompleted(
        publisher.publish("webapp-service")
        .subscribe(_ => println("Record published!"),
          cause => println(s"Could not publish record: ${cause.getMessage}")))
      .subscribe(server => println(s"Server started at http://$host:${server.actualPort}"),
        cause => println(s"Could not start server at http://$host:$port: ${cause.getMessage}"))
  }

}

object Launcher extends App {
  Vertx vertx() deployVerticle nameForVerticle[WebappVerticle]
}

