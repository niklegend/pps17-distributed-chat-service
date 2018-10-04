package it.unibo.dcs.service.webapp.usecases

import it.unibo.dcs.commons.interactor.UseCase
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests.LoginUserRequest
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, UserRepository}
import rx.lang.scala.Observable

final case class LoginResult(loggedUser: User, token: String)

final class LoginUseCase(private[this] val threadExecutor: ThreadExecutor,
                         private[this] val postExecutionThread: PostExecutionThread,
                         private[this] val authRepository: AuthenticationRepository,
                         private[this] val userRepository: UserRepository)
  extends UseCase[LoginResult, LoginUserRequest](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(loginRequest: LoginUserRequest): Observable[LoginResult] = {
    authRepository.loginUser(loginRequest)
      .concatMap(token => userRepository.getUserByUsername(loginRequest.username)
        .map(user => LoginResult(user, token)))
  }

}
