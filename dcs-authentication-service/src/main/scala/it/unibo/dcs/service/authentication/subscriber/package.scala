package it.unibo.dcs.service.authentication

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.commons.VertxWebHelper.{endErrorResponse, getTokenFromHeader, respond, respondWithCode}
import it.unibo.dcs.commons.validation.ErrorTypes.{invalidToken, missingCredentials, unexpectedLogoutError, unexpectedTokenError}
import it.unibo.dcs.service.authentication.interactor.usecases.{LoginUserUseCase, LogoutUserUseCase, RegisterUserUseCase}
import it.unibo.dcs.service.authentication.request.Requests.{LoginUserRequest, LogoutUserRequest, RegisterUserRequest}
import rx.lang.scala.Subscriber

package object subscriber {

  class TokenSubscriber(errorMessage: String, errorCode: Int)(implicit routingContext: RoutingContext)
    extends Subscriber[String] {
    override def onNext(token: String): Unit = respondWithToken(token)

    override def onError(error: Throwable): Unit = {
      error.printStackTrace()
      endErrorResponse(routingContext.response(), HttpResponseStatus.valueOf(errorCode),
        unexpectedTokenError, errorMessage)
    }

    private def respondWithToken(token: String)(implicit context: RoutingContext): Unit = {
      context.response
        .putHeader("content-type", "application/json")
        .setStatusCode(201)
        .end(Json.obj(("token", token)).encodePrettily())
    }
  }

  class LogoutSubscriber(implicit routingContext: RoutingContext) extends Subscriber[Unit] {
    override def onNext(unit: Unit): Unit = respondWithCode(200)

    override def onError(error: Throwable): Unit =
      endErrorResponse(routingContext.response(), HttpResponseStatus.INTERNAL_SERVER_ERROR,
        errorType = unexpectedLogoutError, description = error.getMessage)
  }

  class LogoutValiditySubscriber(logoutUserUseCase: LogoutUserUseCase)
                                        (implicit routingContext: RoutingContext) extends Subscriber[Unit] {

    override def onCompleted(): Unit =
      logoutUserUseCase(LogoutUserRequest(getTokenFromHeader.get)).subscribe(new LogoutSubscriber)

    override def onError(error: Throwable): Unit =
      endErrorResponse(routingContext.response(), HttpResponseStatus.BAD_REQUEST,
        errorType = invalidToken, description = "Invalid token or user not logged in")
  }

  class LoginValiditySubscriber(credentials: (Option[String], Option[String]), loginUserUseCase: LoginUserUseCase)
                                       (implicit routingContext: RoutingContext) extends Subscriber[Unit] {

    override def onCompleted(): Unit =
      loginUserUseCase(LoginUserRequest(credentials._1.get, credentials._2.get))
        .subscribe(new TokenSubscriber("Wrong username or password", 401))

    override def onError(error: Throwable): Unit =
      endErrorResponse(routingContext.response(), HttpResponseStatus.UNAUTHORIZED,
        missingCredentials, "Login credentials not present")
  }

  class RegistrationValiditySubscriber(credentials: (Option[String], Option[String]),
                                       registerUserUseCase: RegisterUserUseCase)
                                      (implicit routingContext: RoutingContext) extends Subscriber[Unit] {

    override def onCompleted(): Unit =
      registerUserUseCase(RegisterUserRequest(credentials._1.get, credentials._2.get))
        .subscribe(new TokenSubscriber("Username already taken", 409))

    override def onError(error: Throwable): Unit =
      endErrorResponse(routingContext.response(), HttpResponseStatus.UNAUTHORIZED,
        missingCredentials, "Registration credentials not present")
  }

  class TokenCheckSubscriber(implicit routingContext: RoutingContext) extends Subscriber[Boolean] {

    override def onNext(tokenIsValid: Boolean): Unit = {
      if (tokenIsValid) {
        respond(200, "Token is valid")
      } else {
        endErrorResponse(routingContext.response(), HttpResponseStatus.UNAUTHORIZED,
          invalidToken, "Invalid token")
      }
    }

    override def onError(error: Throwable): Unit =
      endErrorResponse(routingContext.response(), HttpResponseStatus.UNAUTHORIZED,
        errorType = invalidToken, description = "Invalid token or user not logged in")
  }

}
