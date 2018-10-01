package it.unibo.dcs.service.user

import it.unibo.dcs.commons.interactor.UseCase
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import rx.lang.scala.Observable

final class GetUserUseCase(private[this] val threadExecutor: ThreadExecutor,
                           private[this] val postExecutionThread: PostExecutionThread,
                           private[this] val userRepository: UserRepository)

  extends UseCase[User, String](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(username: String): Observable[User] = ???

}
