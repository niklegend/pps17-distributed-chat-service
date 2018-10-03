package it.unibo.dcs.authentication_service.login

import it.unibo.dcs.authentication_service.common.AuthenticationRepository
import it.unibo.dcs.commons.interactor.UseCase
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import rx.lang.scala.Observable

final class LoginUserUseCase(private[this] val threadExecutor: ThreadExecutor,
                             private[this] val postExecutionThread: PostExecutionThread,
                             private[this] val authRepository: AuthenticationRepository)
  extends UseCase[String, LoginUserRequest](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(request: LoginUserRequest): Observable[String] =
    authRepository.loginUser(request.username, request.password)
}