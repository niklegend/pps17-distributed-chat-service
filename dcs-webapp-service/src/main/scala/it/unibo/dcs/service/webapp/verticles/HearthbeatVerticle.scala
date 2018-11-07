package it.unibo.dcs.service.webapp.verticles

import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.eventbus.Message
import io.vertx.scala.ext.web.Router
import it.unibo.dcs.commons.Publisher
import it.unibo.dcs.commons.VertxHelper.Implicits.RichEventBus
import it.unibo.dcs.commons.service.ServiceVerticle
import it.unibo.dcs.exceptions.UsernameRequiredException
import it.unibo.dcs.service.webapp.verticles.Addresses.users
import it.unibo.dcs.service.webapp.verticles.HearthbeatVerticle.{KEY_USERNAME, getUsername}

final class HearthbeatVerticle extends ServiceVerticle {

  override protected[this] def initializeRouter(router: Router): Unit = ()

  override def start(): Unit = {
    var activeUsers = Set[String]()

    eventBus.address(users.online).handle[JsonObject](usernameHandler { username =>
      activeUsers = activeUsers + username
    })

    eventBus.address(users.offline).handle[JsonObject](usernameHandler { username =>
      activeUsers = activeUsers filterNot { _ == username }
    })

    val hearthbeatRequest: Publisher = eventBus.address(users.hearthbeat.request)

    hearthbeatRequest.publish(Json.emptyObj())

    var responses = Set[String]()

    eventBus.address(users.hearthbeat.response).handle[JsonObject](usernameHandler { username =>
      responses = responses + username
    })

    val userOffline: Publisher = eventBus.address(users.offline)
    vertx.setPeriodic(0L, _ => {
      val diff = activeUsers diff responses
      responses = Set[String]()

      diff foreach { username =>
        userOffline.publish(Json.obj((KEY_USERNAME, username)))
      }

      activeUsers = activeUsers diff diff

      hearthbeatRequest.publish(Json.emptyObj())
    })
  }

  private[this] def usernameHandler(f: String => Unit): Message[JsonObject] => Unit =
    getUsername(_).fold(throw UsernameRequiredException)(f)

}

object HearthbeatVerticle {

  val KEY_USERNAME = "username"

  private def getUsername(message: Message[JsonObject]): Option[String] = {
    val json = message.body

    if (json containsKey KEY_USERNAME)
      Some(json getString KEY_USERNAME)
    else
      None
  }

}
