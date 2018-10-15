package it.unibo.dcs.service.webapp.usecases

import io.vertx.scala.core.Context
import it.unibo.dcs.commons.RxHelper
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.commons.interactor.{ThreadExecutorExecutionContext, UseCase}
import it.unibo.dcs.service.webapp.interaction.Requests.{CheckTokenRequest, DeleteRoomRequest}
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, RoomRepository}
import rx.lang.scala.Observable

final class DeleteRoomUseCase(private[this] val threadExecutor: ThreadExecutor,
                              private[this] val postExecutionThread: PostExecutionThread,
                              private[this] val authRepository: AuthenticationRepository,
                              private[this] val roomRepository: RoomRepository)
  extends UseCase[Unit, DeleteRoomRequest](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(request: DeleteRoomRequest): Observable[Unit] = {
    for {
      _ <- authRepository.checkToken(CheckTokenRequest(request.token))
      _ <- roomRepository.deleteRoom(request)
    } yield Unit
  }

}

object DeleteRoomUseCase {

  def create(authRepository: AuthenticationRepository, roomRepository: RoomRepository)
            (implicit ctx: Context): DeleteRoomUseCase = {
    val threadExecutor: ThreadExecutor = ThreadExecutorExecutionContext(ctx.owner())
    val postExecutionThread: PostExecutionThread = PostExecutionThread(RxHelper.scheduler(ctx))
    new DeleteRoomUseCase(threadExecutor, postExecutionThread, authRepository, roomRepository)
  }
}