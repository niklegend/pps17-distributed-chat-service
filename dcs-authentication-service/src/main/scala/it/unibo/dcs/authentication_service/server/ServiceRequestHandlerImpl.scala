package it.unibo.dcs.authentication_service.server

import io.vertx.core.http.HttpHeaders
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.authentication_service.login.{LoginUserRequest, LoginUserUseCase}
import it.unibo.dcs.authentication_service.logout.{LogoutUserRequest, LogoutUserUseCase}
import it.unibo.dcs.authentication_service.register.{RegisterUserRequest, RegisterUserUseCase}
import it.unibo.dcs.commons.VertxWebHelper._
import rx.lang.scala.Subscriber

class ServiceRequestHandlerImpl(loginUserUseCase: LoginUserUseCase, logoutUserUseCase: LogoutUserUseCase,
                                registerUserUseCase: RegisterUserUseCase) extends ServiceRequestHandler {

  override def handleRegistration(implicit context: RoutingContext): Unit = {
    val credentials = getCredentials
    doIfValidCredentials(credentials,
      registerUserUseCase(RegisterUserRequest(credentials._1.get, credentials._2.get)).subscribe(new TokenSubscriber))
  }

  override def handleLogin(implicit context: RoutingContext): Unit = {
    val credentials = getCredentials
    doIfValidCredentials(credentials,
      loginUserUseCase(LoginUserRequest(credentials._1.get, credentials._2.get)).subscribe(new TokenSubscriber))
  }

  override def handleLogout(implicit context: RoutingContext): Unit = {
    val token = getToken
    doIfDefined(token, logoutUserUseCase(LogoutUserRequest(token.get)).subscribe(new LogoutSubscriber))
  }

  private def getToken(implicit context: RoutingContext): Option[String] = {
    context.request().headers().get(HttpHeaders.AUTHORIZATION.toString)
  }

  private def getUsername(implicit context: RoutingContext): Option[String] = getJsonBodyData("username")

  private def getPassword(implicit context: RoutingContext): Option[String] = getJsonBodyData("password")

  private def getCredentials(implicit context: RoutingContext): (Option[String], Option[String]) =
    (getUsername, getPassword)

  private def doIfValidCredentials(credentials: (Option[String], Option[String]), action: => Unit)
                                  (implicit routingContext: RoutingContext): Unit = {
    if (credentials._1.isDefined && credentials._2.isDefined) {
      action
    } else respondWithCode(400)
  }

  private class TokenSubscriber(implicit routingContext: RoutingContext) extends Subscriber[String] {
    override def onNext(token: String): Unit = respondOkWithJson(Json.obj(("token", token)))

    override def onError(error: Throwable): Unit = respondWithCode(400)
  }

  private class LogoutSubscriber(implicit routingContext: RoutingContext) extends Subscriber[Unit] {
    override def onNext(unit: Unit): Unit = respondOk

    override def onError(error: Throwable): Unit = respondWithCode(400)
  }
}
