package it.unibo.dcs.service.webapp.usecases

import io.vertx.scala.core.Context
import it.unibo.dcs.commons.RxHelper
import it.unibo.dcs.commons.interactor.{ThreadExecutorExecutionContext, UseCase}
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.service.webapp.interaction.Requests.{CheckTokenRequest, NotifyWritingUserRequest}
import it.unibo.dcs.service.webapp.interaction.Results.NotifyWritingUserResult
import it.unibo.dcs.service.webapp.model.Room
import it.unibo.dcs.service.webapp.repositories.AuthenticationRepository
import rx.lang.scala.Observable

/** It represents the functionality of real-time update on the status of user writing.
  * To do so it first checks the provided token. The actual update to the other users
  * will be delivered by a subscriber to this use case.
  *
  * @param threadExecutor      thread executor that will perform the subscription
  * @param postExecutionThread thread that will be notified of the subscription result
  * @param authRepository      authentication repository reference
  * @usecase real-time update on the status of user writing */
final class NotifyWritingInRoomUseCase (private[this] val threadExecutor: ThreadExecutor,
                                private[this] val postExecutionThread: PostExecutionThread,
                                private[this] val authRepository: AuthenticationRepository)
  extends UseCase[NotifyWritingUserResult, NotifyWritingUserRequest](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(request: NotifyWritingUserRequest):
  Observable[NotifyWritingUserResult] =
    authRepository.checkToken(CheckTokenRequest(request.token, request.username))
      .map(_ => NotifyWritingUserResult(request.username))
}

object NotifyWritingInRoomUseCase {

  /** Factory method to create the use case
    *
    * @param authRepository      authentication repository reference
    * @return the use case object*/
  def apply(authRepository: AuthenticationRepository)(implicit ctx: Context): NotifyWritingInRoomUseCase = {
    val threadExecutor: ThreadExecutor = ThreadExecutorExecutionContext(ctx.owner())
    val postExecutionThread: PostExecutionThread = PostExecutionThread(RxHelper.scheduler(ctx))
    new NotifyWritingInRoomUseCase(threadExecutor, postExecutionThread, authRepository)
  }
}