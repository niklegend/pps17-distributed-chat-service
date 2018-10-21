package it.unibo.dcs.service.user

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.lang.scala.ScalaLogger
import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.scala.core.http.HttpServerResponse
import it.unibo.dcs.commons.VertxWebHelper.Implicits.{jsonObjectToString, RichHttpServerResponse}
import it.unibo.dcs.commons.dataaccess.Implicits.dateToString
import it.unibo.dcs.exceptions.ErrorSubscriber
import it.unibo.dcs.service.user.model.User
import it.unibo.dcs.service.user.subscriber.Implicits._
import rx.lang.scala.Subscriber

import scala.language.implicitConversions

package object subscriber {

  final class CreateUserSubscriber(protected override val response: HttpServerResponse) extends Subscriber[User]
    with ErrorSubscriber {

    private[this] val log = ScalaLogger.getLogger(getClass.getName)

    override def onNext(user: User): Unit = {
      val json: JsonObject = user
      log.info(s"Answering with user: $json")
      response.setStatus(HttpResponseStatus.CREATED).end(json)
    }

  }

  final class ValidateUserCreationSubscriber(protected override val response: HttpServerResponse) extends Subscriber[Unit]
      with ErrorSubscriber {

    private[this] val log = ScalaLogger.getLogger(getClass.getName)

    override def onCompleted(): Unit = response.setStatus(HttpResponseStatus.NO_CONTENT).end()

  }

  final class GetUserSubscriber(protected override val response: HttpServerResponse) extends Subscriber[User]
    with ErrorSubscriber {

    private[this] val log = ScalaLogger.getLogger(getClass.getName)

    override def onNext(user: User): Unit = {
      val json: JsonObject = user
      log.info(s"Answering with user: $json")
      response.end(json)
    }

  }

  object Implicits {

    implicit def userToJsonObject(user: User): JsonObject = {
      new JsonObject()
        .put("username", user.username)
        .put("firstName", user.firstName)
        .put("lastName", user.lastName)
        .put("bio", user.bio)
        .put("visible", user.visible)
        .put("lastSeen", dateToString(user.lastSeen))
    }

  }

}
