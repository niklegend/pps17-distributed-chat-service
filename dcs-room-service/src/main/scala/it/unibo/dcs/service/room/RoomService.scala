package it.unibo.dcs.service.room

import io.vertx.core.{AbstractVerticle, Context, Vertx}
import io.vertx.scala.ext.web.Router
import it.unibo.dcs.commons.service.ServiceVerticle

final class RoomService extends ServiceVerticle {

  private var host: String = _
  private var port: Int = _

  override def init(jVertx: Vertx, context: Context, verticle: AbstractVerticle): Unit = {
    super.init(jVertx, context, verticle)
    val config = context.config
    host = config.getString("host")
    port = config.getInteger("port")
  }

  override protected def initializeRouter(router: Router): Unit = {
    router.post("/users")
      .consumes("application/json")
      .produces("application/json")
      .handler(rc => ???)
  }

  override def start(): Unit = startHttpServer(host, port)
    .subscribe(server => println(s"Server started at http://$host:${server.actualPort}"),
               cause => println(s"Could not start server ar http://$host:$port, ${cause.getMessage}"))

}
