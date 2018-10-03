package it.unibo.dcs.authentication_service.logout

import it.unibo.dcs.authentication_service.MocksForUseCases._
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import rx.lang.scala.{Observable, Subscriber}

class LogoutUserUseCaseTest extends FlatSpec with MockFactory {

  val request = LogoutUserRequest("ale")
  val expectedResult: Unit = Unit

  val subscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
  val logoutUserUseCase = new LogoutUserUseCase(threadExecutor, postExecutionThread, authRepository)

  it should "logout the user when the use case is executed" in {
    (authRepository logoutUser _) expects request.username returns (Observable just expectedResult)

    logoutUserUseCase(request).subscribe(subscriber)

    (subscriber onNext _) verify expectedResult once()
    (() => subscriber onCompleted) verify() once()
  }

}
