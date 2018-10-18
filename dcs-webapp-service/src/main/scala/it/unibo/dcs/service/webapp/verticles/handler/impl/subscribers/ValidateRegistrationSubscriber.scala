package it.unibo.dcs.service.webapp.verticles.handler.impl.subscribers

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.scala.core.Context
import io.vertx.scala.core.http.HttpServerResponse
import it.unibo.dcs.commons.service.ErrorHandler
import it.unibo.dcs.exceptions.RegistrationValidityResponseException
import it.unibo.dcs.service.webapp.interaction.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.interaction.Results.Implicits._
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, RoomRepository, UserRepository}
import it.unibo.dcs.service.webapp.usecases.RegisterUserUseCase
import rx.lang.scala.Subscriber


final class ValidateRegistrationSubscriber(private[this] val response: HttpServerResponse,
                                           private[this] val request: RegisterUserRequest,
                                           private[this] val authRepository: AuthenticationRepository,
                                           private[this] val userRepository: UserRepository,
                                           private[this] val roomRepository: RoomRepository,
                                           private[this] implicit val ctx: Context)
  extends Subscriber[Unit] with ErrorHandler {


  override def onCompleted(): Unit = {
    val useCase = RegisterUserUseCase.create(authRepository, userRepository, roomRepository)
    useCase(request) subscribe (result => response end result)
  }

  override def onError(error: Throwable): Unit = error match {
    case RegistrationValidityResponseException(message) =>
      endErrorResponse(response, HttpResponseStatus.NO_CONTENT, errorType = "MISSING_RESPONSE_BODY", message)
  }
}

object ValidateRegistrationSubscriber {
  def apply(response: HttpServerResponse,
            request: RegisterUserRequest,
            authRepository: AuthenticationRepository,
            userRepository: UserRepository,
            roomRepository: RoomRepository)(implicit ctx: Context): ValidateRegistrationSubscriber =
    new ValidateRegistrationSubscriber(response, request, authRepository, userRepository, roomRepository, ctx)
}
