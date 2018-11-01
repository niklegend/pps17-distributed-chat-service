package it.unibo.dcs.service.authentication.server.handlers

import io.vertx.scala.ext.web.RoutingContext
import it.unibo.dcs.commons.VertxWebHelper._
import it.unibo.dcs.service.authentication.interactor.usecases._
import it.unibo.dcs.service.authentication.request.Requests._
import it.unibo.dcs.service.authentication.server.containers.{UseCaseContainer, ValidationContainer}
import it.unibo.dcs.service.authentication.subscriber._

class ServiceRequestHandlerImpl(loginUserUseCase: LoginUserUseCase, logoutUserUseCase: LogoutUserUseCase,
                                registerUserUseCase: RegisterUserUseCase, checkTokenUseCase: CheckTokenUseCase,
                                deleteUserUseCase: DeleteUserUseCase) extends ServiceRequestHandler {

  override def handleRegistration(implicit context: RoutingContext): Unit = {
    val credentials: (Option[String], Option[String]) = getCredentials
    val request = RegisterUserRequest(credentials._1.get, credentials._2.get)
    registerUserUseCase(request, new TokenSubscriber(context.response))
  }

  override def handleLogin(implicit context: RoutingContext): Unit = {
    val credentials = getCredentials
    val request = LoginUserRequest(credentials._1.get, credentials._2.get)
    loginUserUseCase(request, new TokenSubscriber(context.response))
  }

  override def handleLogout(implicit context: RoutingContext): Unit = {
    val request = LogoutUserRequest(getTokenFromHeader.get)
    /* Request check before executing logout*/
    logoutUserUseCase(request, new OkSubscriber(context.response))
  }

  override def handleTokenCheck(implicit context: RoutingContext): Unit = {
    val request = CheckTokenRequest(getTokenFromHeader.get)
    checkTokenUseCase(request, new TokenCheckSubscriber(context.response()))
  }

  override def handleUserDeletion(implicit context: RoutingContext): Unit = {
    val request = DeleteUserRequest(getContextData("username").get, getTokenFromHeader.get)
    deleteUserUseCase(request, new OkSubscriber(context.response))
  }

  private def getUsername(implicit context: RoutingContext): Option[String] =
    getJsonBodyData("username")

  private def getPassword(implicit context: RoutingContext): Option[String] =
    getJsonBodyData("password")

  private def getCredentials(implicit context: RoutingContext): (Option[String], Option[String]) =
    (getUsername, getPassword)

}

object ServiceRequestHandlerImpl {

  def apply(useCases: UseCaseContainer, validations: ValidationContainer): ServiceRequestHandlerImpl = {
    val loginUserUseCase = useCases.loginUserUseCase
    val logoutUserUseCase = useCases.logoutUserUseCase
    val registerUserUseCase = useCases.registerUserUseCase
    val checkTokenUseCase = useCases.checkTokenUseCase
    val deleteUserUseCase = useCases.deleteUserUseCase

    new ServiceRequestHandlerImpl(loginUserUseCase, logoutUserUseCase, registerUserUseCase, checkTokenUseCase,
      deleteUserUseCase)
  }
}
