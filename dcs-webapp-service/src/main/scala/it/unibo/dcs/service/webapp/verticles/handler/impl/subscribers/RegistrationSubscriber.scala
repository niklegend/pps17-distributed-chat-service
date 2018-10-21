package it.unibo.dcs.service.webapp.verticles.handler.impl.subscribers

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.scala.core.Context
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.commons.VertxWebHelper._
import it.unibo.dcs.commons.validation.ErrorTypes._
import it.unibo.dcs.exceptions._
import it.unibo.dcs.service.webapp.interaction.Requests.DeleteUserRequest
import it.unibo.dcs.service.webapp.interaction.Results.Implicits._
import it.unibo.dcs.service.webapp.interaction.Results.RegisterResult
import it.unibo.dcs.service.webapp.repositories.AuthenticationRepository
import rx.lang.scala.Subscriber


final class RegistrationSubscriber(private[this] val routingContext: RoutingContext,
                                   private[this] val authRepository: AuthenticationRepository,
                                   private[this] implicit val ctx: Context) extends Subscriber[RegisterResult] {


  override def onNext(result: RegisterResult): Unit = routingContext.response().end(result)

  override def onError(error: Throwable): Unit = error match {

    case RegistrationResponseException(message) => ()
    case AuthServiceErrorException(errorJson) => ()

    case UserServiceErrorException(errorResponseJson, username, token) =>
      implicit val routingContext: RoutingContext = this.routingContext
      rollbackAuthRepository(username, token)(new RollbackOnServiceErrorSubscriber())

      class RollbackOnServiceErrorSubscriber(implicit routingContext: RoutingContext) extends Subscriber[Unit] {
        override def onCompleted(): Unit =
          respond(HttpResponseStatus.BAD_REQUEST.code(), errorResponseJson.encodePrettily())

        override def onError(error: Throwable): Unit = ???
      }

    case UserCreationResponseException(message, username, token) =>
      implicit val routingContext: RoutingContext = this.routingContext
      rollbackAuthRepository(username, token)(new RollbackOnWrongResponseSubscriber())

      class RollbackOnWrongResponseSubscriber(implicit routingContext: RoutingContext) extends Subscriber[Unit] {
        override def onCompleted(): Unit =
          endErrorResponse(routingContext.response(), HttpResponseStatus.INTERNAL_SERVER_ERROR,
            userServiceWrongResponse, message)

        override def onError(error: Throwable): Unit = ???
      }

    case RegistrationResponseException(message) => ()
    case RoomServiceErrorException(errorJson) => ()
  }

  /* Rollback changes previously performed in Authentication service */
  private def rollbackAuthRepository(username: String, token: String)(subscriber: Subscriber[Unit]) = {
    authRepository.deleteUser(DeleteUserRequest(username, token)).subscribe(subscriber)
  }
}

object RegistrationSubscriber {
  def apply(routingContext: RoutingContext,
            authRepository: AuthenticationRepository)(implicit ctx: Context): RegistrationSubscriber =
    new RegistrationSubscriber(routingContext, authRepository, ctx)
}
