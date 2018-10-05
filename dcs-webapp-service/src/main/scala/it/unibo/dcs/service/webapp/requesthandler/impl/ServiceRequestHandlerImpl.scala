package it.unibo.dcs.service.webapp.requesthandler.impl

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.service.webapp.repositories.Requests.Implicits._
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, UserRepository}
import it.unibo.dcs.service.webapp.requesthandler.ServiceRequestHandler
import it.unibo.dcs.service.webapp.usecases.RegisterUserUseCase

class ServiceRequestHandlerImpl(private val userRepository: UserRepository,
                                private val authRepository: AuthenticationRepository)
  extends ServiceRequestHandler {

  override def handleRegistration(context: RoutingContext): Unit =
    if (context.getBodyAsJson().isDefined) {
      val registerRequest: JsonObject = context.getBodyAsJson().get
      // to write more clearly and return a json result
      new RegisterUserUseCase(null, null,
        authRepository, userRepository) apply registerRequest subscribe (registerResult =>
        context.response().end(registerResult.toString))
    } else {
      context.response().setStatusCode(HttpResponseStatus.NOT_ACCEPTABLE.code)
        .end("Missing registration information")
    }

  override def handleLogout(context: RoutingContext): Unit = ???

  override def handleLogin(context: RoutingContext): Unit = ???
}
