package it.unibo.dcs.authentication_service.server
import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.authentication_service.login.{LoginUserRequest, LoginUserUseCase}
import it.unibo.dcs.authentication_service.logout.{LogoutUserRequest, LogoutUserUseCase}
import it.unibo.dcs.authentication_service.register.{RegisterUserRequest, RegisterUserUseCase}
import it.unibo.dcs.commons.VertxWebHelper._

class ServiceRequestHandlerImpl(loginUserUseCase: LoginUserUseCase, logoutUserUseCase: LogoutUserUseCase,
                                registerUserUseCase: RegisterUserUseCase) extends ServiceRequestHandler {
  // TODO: pass a subscriber to the use cases, to respond correctly to the client

  override def handleRegistration(implicit context: RoutingContext): Unit = {
    val credentials = getCredentials
    doIfValidCredentials(credentials, registerUserUseCase(RegisterUserRequest(credentials._1.get, credentials._2.get)))
  }

  override def handleLogin(implicit context: RoutingContext): Unit = {
    val credentials = getCredentials
    doIfValidCredentials(credentials, loginUserUseCase(LoginUserRequest(credentials._1.get, credentials._2.get)))
  }

  override def handleLogout(implicit context: RoutingContext): Unit = {
    val username = getUsername
    doIfDefined(username, logoutUserUseCase(LogoutUserRequest(username.get)))
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
}
