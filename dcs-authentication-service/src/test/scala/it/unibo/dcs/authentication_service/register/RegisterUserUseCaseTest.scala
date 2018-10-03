package it.unibo.dcs.authentication_service.register

import it.unibo.dcs.authentication_service.MocksForUseCases._
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import rx.lang.scala.{Observable, Subscriber}

class RegisterUserUseCaseTest extends FlatSpec with MockFactory {

  val request = RegisterUserRequest("ale", "123456")
  val expectedResult = "token"

  val subscriber: Subscriber[String] = stub[Subscriber[String]]

  val registerUserUseCase = new RegisterUserUseCase(threadExecutor, postExecutionThread, authRepository)

  it should "register the user when the use case is executed" in {
    (authRepository createUser(_, _)) expects (request.username, request.password) returns (Observable just expectedResult)

    registerUserUseCase(request).subscribe(subscriber)

    (subscriber onNext _) verify expectedResult once()
    (() => subscriber onCompleted) verify() once()
  }

}