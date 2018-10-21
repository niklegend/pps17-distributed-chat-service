package it.unibo.dcs.service.webapp.verticles.handler.impl.subscribers

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.scala.core.Context
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.commons.VertxWebHelper.{endErrorResponse, respond}
import it.unibo.dcs.commons.validation.ErrorTypes.userServiceWrongResponse
import it.unibo.dcs.exceptions.{RoomCreationResponseException, RoomServiceErrorException, UserServiceErrorException}
import it.unibo.dcs.service.webapp.interaction.Requests.DeleteUserRequest
import it.unibo.dcs.service.webapp.interaction.Results.RoomCreationResult
import it.unibo.dcs.service.webapp.repositories.AuthenticationRepository
import rx.lang.scala.Subscriber
import it.unibo.dcs.service.webapp.interaction.Results.Implicits._


final class RoomCreationSubscriber(private[this] val routingContext: RoutingContext,
                                   private[this] val authRepository: AuthenticationRepository,
                                   private[this] implicit val ctx: Context) extends Subscriber[RoomCreationResult] {


  override def onNext(result: RoomCreationResult): Unit = routingContext.response().end(result)

  override def onError(error: Throwable): Unit = error match {

    case RoomServiceErrorException(errorResponseJson) => ()

    case RoomCreationResponseException(message) => ()
  }

}

object RoomCreationSubscriber {
  def apply(routingContext: RoutingContext,
            authRepository: AuthenticationRepository)(implicit ctx: Context): RoomCreationSubscriber =
    new RoomCreationSubscriber(routingContext, authRepository, ctx)
}