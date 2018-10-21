package it.unibo.dcs.authentication_service.server

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.lang.scala.json.Json
import io.vertx.scala.core.http.HttpServerResponse
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.authentication_service.interactor._
import it.unibo.dcs.authentication_service.request.{CheckTokenRequest, LoginUserRequest, LogoutUserRequest, RegisterUserRequest}
import it.unibo.dcs.commons.VertxWebHelper.Implicits.{RichHttpServerResponse, jsonObjectToString}
import it.unibo.dcs.commons.VertxWebHelper._
import it.unibo.dcs.exceptions.ErrorSubscriber
import rx.lang.scala.Subscriber

class ServiceRequestHandlerImpl(loginUserUseCase: LoginUserUseCase, logoutUserUseCase: LogoutUserUseCase,
                                registerUserUseCase: RegisterUserUseCase, checkTokenUseCase: CheckTokenUseCase,
                                logoutUserValidation: LogoutUserValidation)
  extends ServiceRequestHandler {

  override def handleRegistration(implicit context: RoutingContext): Unit = {
    val credentials = getCredentials
    doIfValidCredentials(credentials,
      registerUserUseCase(RegisterUserRequest(credentials._1.get, credentials._2.get))
        .subscribe(new TokenSubscriber(context.response())))
  }

  override def handleLogin(implicit context: RoutingContext): Unit = {
    val credentials = getCredentials
    doIfValidCredentials(credentials,
      loginUserUseCase(LoginUserRequest(credentials._1.get, credentials._2.get))
        .subscribe(new TokenSubscriber(context.response())))
  }

  override def handleLogout(implicit context: RoutingContext): Unit = {
    val token = getTokenFromHeader
    doIfDefined(token, logoutUserUseCase(LogoutUserRequest(token.get), new LogoutSubscriber(context.response)))
  }

  override def handleTokenCheck(implicit context: RoutingContext): Unit = {
    val token = getTokenFromHeader
    doIfDefined(token, checkTokenUseCase(CheckTokenRequest(token.get))
      .subscribe(tokenIsValid => if (tokenIsValid) {
        respond(HttpResponseStatus.OK)
      } else {
        respond(HttpResponseStatus.UNAUTHORIZED)
      }))
  }

  override def handleCheckLogout(implicit context: RoutingContext): Unit = {
    val token = getTokenFromHeader
    doIfDefined(token, logoutUserValidation(LogoutUserRequest(token.get), new LogoutValiditySubscriber(context.response())))
  }

  private def getUsername(implicit context: RoutingContext): Option[String] = getJsonBodyData("username")

  private def getPassword(implicit context: RoutingContext): Option[String] = getJsonBodyData("password")

  private def getCredentials(implicit context: RoutingContext): (Option[String], Option[String]) =
    (getUsername, getPassword)

  private def doIfValidCredentials(credentials: (Option[String], Option[String]), action: => Unit)
                                  (implicit routingContext: RoutingContext): Unit = {
    if (credentials._1.isDefined && credentials._2.isDefined) {
      action
    } else respond(HttpResponseStatus.UNAUTHORIZED)
  }

  private class TokenSubscriber(protected override val response: HttpServerResponse)
    extends Subscriber[String]
    with ErrorSubscriber {

    override def onNext(token: String): Unit = response.end(Json.obj(("token", token)))

  }

  private class LogoutSubscriber(protected override val response: HttpServerResponse) extends Subscriber[Unit]
    with ErrorSubscriber {

    override def onCompleted(): Unit = response.setStatus(HttpResponseStatus.RESET_CONTENT).end()

  }

  private class LogoutValiditySubscriber(protected override val response: HttpServerResponse) extends Subscriber[Unit]
    with ErrorSubscriber {

    override def onCompleted(): Unit = response.setStatus(HttpResponseStatus.OK).end()

  }

}

object ServiceRequestHandlerImpl {
  def apply(loginUserUseCase: LoginUserUseCase, logoutUserUseCase: LogoutUserUseCase,
            registerUserUseCase: RegisterUserUseCase, checkTokenUseCase: CheckTokenUseCase,
            logoutUserValidation: LogoutUserValidation) =
    new ServiceRequestHandlerImpl(loginUserUseCase, logoutUserUseCase, registerUserUseCase, checkTokenUseCase,
      logoutUserValidation)
}
