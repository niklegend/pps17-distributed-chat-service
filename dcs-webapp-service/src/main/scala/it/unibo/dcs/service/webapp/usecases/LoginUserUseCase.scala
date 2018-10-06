package it.unibo.dcs.service.webapp.usecases

import it.unibo.dcs.commons.interactor.{ThreadExecutorExecutionContext, UseCase}
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import Results.LoginResult
import io.vertx.scala.core.Context
import io.vertx.scala.core.Vertx.vertx
import it.unibo.dcs.commons.RxHelper
import it.unibo.dcs.service.webapp.repositories.Requests.LoginUserRequest
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, UserRepository}
import rx.lang.scala.Observable


final class LoginUserUseCase(private[this] val threadExecutor: ThreadExecutor,
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

object LoginUserUseCase {
  def create(implicit ctx: Context): LoginUserUseCase = {
    val threadExecutor: ThreadExecutor = ThreadExecutorExecutionContext(ctx.owner())
    val postExecutionThread: PostExecutionThread = PostExecutionThread(RxHelper.scheduler(ctx))
    new LoginUserUseCase(threadExecutor, postExecutionThread, AuthenticationRepository(), UserRepository())
  }
}
