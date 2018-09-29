package it.unibo.dcs.service.user

import it.unibo.dcs.commons.use_case.UseCase
import it.unibo.dcs.commons.use_case.executor.{PostExecutionThread, ThreadExecutor}
import rx.lang.scala.Observable

final case class GetUserUseCase(threadExecutor: ThreadExecutor,
                                postExecutionThread: PostExecutionThread,
                                userRepository: UserRepository) extends UseCase[User, String](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(username: String): Observable[User] = ???

}
