package it.unibo.dcs.service.webapp.usecases

import it.unibo.dcs.commons.interactor.UseCase
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.AuthenticationRepository
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import rx.lang.scala.Observable

final class RegisterUserUseCase(private[this] val threadExecutor: ThreadExecutor,
                                private[this] val postExecutionThread: PostExecutionThread,
                                private[this] val authRepository: AuthenticationRepository)
  extends UseCase[User, RegisterUserRequest](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(parameters: RegisterUserRequest): Observable[User] = ???
}
