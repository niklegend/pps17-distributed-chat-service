package it.unibo.dcs.service.webapp.verticles.requesthandler.impl

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.Context
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.service.webapp.repositories.Requests.Implicits._
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, UserRepository}
import it.unibo.dcs.service.webapp.usecases.Results.Implicits._
import it.unibo.dcs.service.webapp.usecases.{LoginUserUseCase, LogoutUserUseCase, RegisterUserUseCase}
import it.unibo.dcs.service.webapp.verticles.requesthandler.ServiceRequestHandler

import scala.language.postfixOps

final class ServiceRequestHandlerImpl(private val userRepository: UserRepository,
                                      private val authRepository: AuthenticationRepository)
  extends ServiceRequestHandler {

  override def handleRegistration(context: RoutingContext)(implicit ctx: Context): Unit = {
    if (context.getBodyAsJson().isDefined) {
      val registerRequest: JsonObject = context.getBodyAsJson().get
      val useCase = RegisterUserUseCase.create(authRepository, userRepository)
      useCase(registerRequest) subscribe (result => context response() end result)
    } else {
      context.response().setStatusCode(HttpResponseStatus.NOT_ACCEPTABLE.code)
        .end("Missing registration information")
    }
  }

  override def handleLogout(context: RoutingContext)(implicit ctx: Context): Unit = {
    if (context.getBodyAsJson().isDefined) {
      val logoutRequest: JsonObject = context.getBodyAsJson().get
      val useCase = LogoutUserUseCase.create(authRepository)
      useCase(logoutRequest) subscribe (_ => context response() end)
    } else {
      context.response().setStatusCode(HttpResponseStatus.NOT_ACCEPTABLE.code)
        .end("Missing logout information")
    }
  }

  override def handleLogin(context: RoutingContext)(implicit ctx: Context): Unit = {
    if (context.getBodyAsJson().isDefined) {
      val loginRequest: JsonObject = context.getBodyAsJson().get
      val useCase = LoginUserUseCase.create(authRepository, userRepository)
      useCase(loginRequest) subscribe (result => context response() end result)
    } else {
      context.response().setStatusCode(HttpResponseStatus.NOT_ACCEPTABLE.code)
        .end("Missing logout information")
    }
  }

}
