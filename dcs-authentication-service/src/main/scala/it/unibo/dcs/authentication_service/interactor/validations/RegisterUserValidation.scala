package it.unibo.dcs.authentication_service.interactor.validations

import it.unibo.dcs.authentication_service.request.RegisterUserRequest
import it.unibo.dcs.commons.interactor.Validation
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.commons.validation.Validator
import rx.lang.scala.Observable

final class RegisterUserValidation(private[this] val threadExecutor: ThreadExecutor,
                                   private[this] val postExecutionThread: PostExecutionThread,
                                   private[this] val validator: Validator[RegisterUserRequest])
  extends Validation[Unit, RegisterUserRequest](threadExecutor, postExecutionThread) {

  override protected[this] def createObservable(request: RegisterUserRequest): Observable[Unit] =
    validator.validate(request)
}

object RegisterUserValidation {
  def apply(threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread,
            validator: Validator[RegisterUserRequest]): RegisterUserValidation =
    new RegisterUserValidation(threadExecutor, postExecutionThread, validator)
}