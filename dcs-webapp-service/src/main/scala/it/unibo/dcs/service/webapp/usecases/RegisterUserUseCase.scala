package it.unibo.dcs.service.webapp.usecases

<<<<<<< HEAD
import io.vertx.scala.core.Context
import it.unibo.dcs.commons.RxHelper
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.commons.interactor.{ThreadExecutorExecutionContext, UseCase}
import it.unibo.dcs.service.webapp.interaction.Requests.RegisterUserRequest
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, UserRepository}
import it.unibo.dcs.service.webapp.interaction.Results.RegisterResult
=======
import it.unibo.dcs.commons.interactor.UseCase
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.AuthenticationRepository
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
import rx.lang.scala.Observable

final class RegisterUserUseCase(private[this] val threadExecutor: ThreadExecutor,
                                private[this] val postExecutionThread: PostExecutionThread,
<<<<<<< HEAD
                                private[this] val authRepository: AuthenticationRepository,
                                private[this] val userRepository: UserRepository)
  extends UseCase[RegisterResult, RegisterUserRequest](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(registerRequest: RegisterUserRequest): Observable[RegisterResult] = {
    /* Monadic style */
    for {
      token <- authRepository.registerUser(registerRequest)
      user <- userRepository.registerUser(registerRequest)
    } yield RegisterResult(user, token)
  }
}

object RegisterUserUseCase {
  def create(authRepository: AuthenticationRepository,
             userRepository: UserRepository)(implicit ctx: Context): RegisterUserUseCase = {
    val threadExecutor: ThreadExecutor = ThreadExecutorExecutionContext(ctx.owner())
    val postExecutionThread: PostExecutionThread = PostExecutionThread(RxHelper.scheduler(ctx))
    new RegisterUserUseCase(threadExecutor, postExecutionThread, authRepository, userRepository)
  }
}
=======
                                private[this] val authRepository: AuthenticationRepository)
  extends UseCase[User, RegisterUserRequest](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(registerUser: RegisterUserRequest): Observable[User] = ???
}
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
