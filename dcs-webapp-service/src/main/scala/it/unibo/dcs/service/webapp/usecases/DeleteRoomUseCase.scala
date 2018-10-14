package it.unibo.dcs.service.webapp.usecases

import io.vertx.scala.core.Context
import it.unibo.dcs.commons.RxHelper
import it.unibo.dcs.commons.interactor.{ThreadExecutorExecutionContext, UseCase}
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.service.webapp.repositories.Requests.DeleteRoomRequest
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, UserRepository}
import it.unibo.dcs.service.webapp.usecases.Results.LoginResult
import rx.lang.scala.Observable

final class DeleteRoomUseCase(private[this] val threadExecutor: ThreadExecutor,
                             private[this] val postExecutionThread: PostExecutionThread,
                             private[this] val authRepository: AuthenticationRepository,
                             private[this] val userRepository: UserRepository)
  extends UseCase[Unit, DeleteRoomRequest](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(request: DeleteRoomRequest): Observable[Unit] = { //TODO
    for {
      token <- authRepository.loginUser(request)
      user <- userRepository.getUserByUsername(request.username)
    } yield LoginResult(user, token)
  }

}

object DeleteRoomUseCase { //TODO
  def create(authRepository: AuthenticationRepository,
             userRepository: UserRepository)(implicit ctx: Context): LoginUserUseCase = {
    val threadExecutor: ThreadExecutor = ThreadExecutorExecutionContext(ctx.owner())
    val postExecutionThread: PostExecutionThread = PostExecutionThread(RxHelper.scheduler(ctx))
    new LoginUserUseCase(threadExecutor, postExecutionThread, authRepository, userRepository)
  }
}