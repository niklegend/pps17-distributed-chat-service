package it.unibo.dcs.service.webapp.requesthandler.impl

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.core.Context
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.service.webapp.repositories.Requests.Implicits._
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, UserRepository}
import it.unibo.dcs.service.webapp.requesthandler.ServiceRequestHandler
import it.unibo.dcs.service.webapp.usecases.Results.Implicits._
import it.unibo.dcs.service.webapp.usecases.{LoginUserUseCase, LogoutUserUseCase, RegisterUserUseCase}

import scala.language.postfixOps

final class ServiceRequestHandlerImpl(private val userRepository: UserRepository,
                                      private val authRepository: AuthenticationRepository)
  extends ServiceRequestHandler {

  override def handleRegistration(ctx: Context, context: RoutingContext): Unit = {
    if (context.getBodyAsJson().isDefined) {
      val registerRequest: JsonObject = context.getBodyAsJson().get
      implicit val vertxContext: Context = ctx
      val useCase = RegisterUserUseCase.create
      useCase(registerRequest) subscribe (result => context response() end result)
    } else {
      context.response().setStatusCode(HttpResponseStatus.NOT_ACCEPTABLE.code)
        .end("Missing registration information")
    }
  }

  override def handleLogout(ctx: Context, context: RoutingContext): Unit = {
    if (context.getBodyAsJson().isDefined) {
      val logoutRequest: JsonObject = context.getBodyAsJson().get
      implicit val vertxContext: Context = ctx
      val useCase = LogoutUserUseCase.create
      useCase(logoutRequest) subscribe (_ => context response() end)
    } else {
      context.response().setStatusCode(HttpResponseStatus.NOT_ACCEPTABLE.code)
        .end("Missing logout information")
    }
  }

  override def handleLogin(ctx: Context, context: RoutingContext): Unit = {
    if (context.getBodyAsJson().isDefined) {
      val loginRequest: JsonObject = context.getBodyAsJson().get
      implicit val vertxContext: Context = ctx
      val useCase = LoginUserUseCase.create
      useCase(loginRequest) subscribe (result => context response() end result)
    } else {
      context.response().setStatusCode(HttpResponseStatus.NOT_ACCEPTABLE.code)
        .end("Missing logout information")
    }
  }

  // private def handleRequestInfo(success: RoutingContext => Subscription, failure: RoutingContext => Unit,
  //                               body: Option[JsonObject]) = if (body.isDefined) success() else failure()

}
