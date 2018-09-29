package it.unibo.dcs.service.user

import it.unibo.dcs.commons.use_case.UseCase
import it.unibo.dcs.commons.use_case.executor.{PostExecutionThread, ThreadExecutor}
import rx.lang.scala.Observable

final case class CreateUserUseCase(threadExecutor: ThreadExecutor,
                                   postExecutionThread: PostExecutionThread,
                                   userRepository: UserRepository) extends UseCase[User, CreateUserRequest](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(request: CreateUserRequest): Observable[User] = ???

}
