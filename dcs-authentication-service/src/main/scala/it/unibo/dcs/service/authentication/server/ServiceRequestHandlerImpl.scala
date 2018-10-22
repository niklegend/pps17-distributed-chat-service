package it.unibo.dcs.service.authentication.server

import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.service.authentication.request.Requests._
import it.unibo.dcs.commons.VertxWebHelper._
import it.unibo.dcs.service.authentication.interactor.usecases._
import it.unibo.dcs.service.authentication.interactor.validations._
import it.unibo.dcs.service.authentication.server.containers.{UseCaseContainer, ValidationContainer}
import it.unibo.dcs.service.authentication.subscriber._

class ServiceRequestHandlerImpl(loginUserUseCase: LoginUserUseCase, logoutUserUseCase: LogoutUserUseCase,
                                registerUserUseCase: RegisterUserUseCase, checkTokenUseCase: CheckTokenUseCase,
                                logoutUserValidation: LogoutUserValidation,
                                registrationValidation: RegisterUserValidation,
                                loginValidation: LoginUserValidation) extends ServiceRequestHandler {

  override def handleRegistration(implicit context: RoutingContext): Unit = {
    val credentials: (Option[String], Option[String]) = getCredentials
    registrationValidation(RegisterUserRequest(credentials._1.get, credentials._2.get))
      .subscribe(new RegistrationValiditySubscriber(credentials, registerUserUseCase))
  }

  override def handleLogin(implicit context: RoutingContext): Unit = {
    val credentials = getCredentials
    loginValidation(LoginUserRequest(credentials._1.get, credentials._2.get))
      .subscribe(new LoginValiditySubscriber(credentials, loginUserUseCase))
  }

  override def handleLogout(implicit context: RoutingContext): Unit = {
    /* Request check before executing logout*/
    logoutUserValidation(LogoutUserRequest(getTokenFromHeader.get))
      .subscribe(new LogoutValiditySubscriber(logoutUserUseCase))
  }

  override def handleTokenCheck(implicit context: RoutingContext): Unit = {
    val token = getTokenFromHeader
    doIfDefined(token, checkTokenUseCase(CheckTokenRequest(token.get)).subscribe(new TokenCheckSubscriber()))
  }

  private def getUsername(implicit context: RoutingContext): Option[String] =
    getJsonBodyData("username")

  private def getPassword(implicit context: RoutingContext): Option[String] =
    getJsonBodyData("password")

  private def getCredentials(implicit context: RoutingContext): (Option[String], Option[String]) =
    (getUsername, getPassword)

}

object ServiceRequestHandlerImpl {

  def apply(useCases: UseCaseContainer, validations: ValidationContainer) = {
    val loginUserUseCase = useCases.loginUserUseCase
    val logoutUserUseCase = useCases.logoutUserUseCase
    val registerUserUseCase = useCases.registerUserUseCase
    val checkTokenUseCase = useCases.checkTokenUseCase
    val logoutValidation = validations.logoutUserValidation
    val loginValidation = validations.loginUserValidation
    val registrationValidation = validations.registrationValidation

    new ServiceRequestHandlerImpl(loginUserUseCase, logoutUserUseCase, registerUserUseCase, checkTokenUseCase,
      logoutValidation, registrationValidation, loginValidation)
  }
}
