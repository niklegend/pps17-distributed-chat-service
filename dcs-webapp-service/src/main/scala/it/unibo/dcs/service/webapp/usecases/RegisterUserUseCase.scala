package it.unibo.dcs.service.webapp.usecases

import it.unibo.dcs.commons.interactor.UseCase
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, UserRepository}
import it.unibo.dcs.service.webapp.usecases.Results.RegisterResult
import rx.lang.scala.Observable

final class RegisterUserUseCase(private[this] val threadExecutor: ThreadExecutor,
                                private[this] val postExecutionThread: PostExecutionThread,
                                private[this] val authRepository: AuthenticationRepository,
                                private[this] val userRepository: UserRepository)
  extends UseCase[RegisterResult, RegisterUserRequest](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(registerRequest: RegisterUserRequest): Observable[RegisterResult] = {
    authRepository.registerUser(registerRequest)
      .concatMap(token => userRepository.registerUser(registerRequest)
        .map(user => RegisterResult(user, token)))
  }
}
