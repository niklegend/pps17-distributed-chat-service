package it.unibo.dcs.service.webapp.verticles

import io.vertx.scala.ext.web.Router
import it.unibo.dcs.commons.service.ServiceVerticle
import it.unibo.dcs.commons.VertxHelper.Implicits.RichEventBus
import Addresses.users
import io.vertx.lang.scala.json.Json
import it.unibo.dcs.commons.Publisher

final class HearthbeatVerticle extends ServiceVerticle {

  override protected[this] def initializeRouter(router: Router): Unit = ()

  override def start(): Unit = {
    val hearthbeatRequest: Publisher = eventBus.address(users.hearthbeat.request)
    vertx.setPeriodic(0L, _ => {
      hearthbeatRequest.publish(Json.emptyObj())
    })

    // TODO
    eventBus.address(users.online)
    eventBus.address(users.offline)
    eventBus.address(users.hearthbeat.response)
  }

}
