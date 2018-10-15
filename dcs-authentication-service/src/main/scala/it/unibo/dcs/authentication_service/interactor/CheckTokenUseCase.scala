package it.unibo.dcs.authentication_service.interactor

import it.unibo.dcs.authentication_service.repository.AuthenticationRepository
import it.unibo.dcs.authentication_service.request.CheckTokenRequest
import it.unibo.dcs.commons.interactor.UseCase
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import rx.lang.scala.Observable

final class CheckTokenUseCase(private[this] val threadExecutor: ThreadExecutor,
                              private[this] val postExecutionThread: PostExecutionThread,
                              private[this] val authRepository: AuthenticationRepository)
  extends UseCase[Boolean, CheckTokenRequest](threadExecutor, postExecutionThread) {

  override def createObservable(request: CheckTokenRequest): Observable[Boolean] =
    authRepository.isTokenValid(request.token)
}

object CheckTokenUseCase {
  def apply(threadExecutor: ThreadExecutor, postExecutionThread: PostExecutionThread,
            authRepository: AuthenticationRepository) =
    new CheckTokenUseCase(threadExecutor, postExecutionThread, authRepository)
}