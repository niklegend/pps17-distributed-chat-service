package it.unibo.dcs.service.user

import java.util.Date

import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import rx.lang.scala.{Observable, Subscriber}

class CreateUserUseCaseTest extends FlatSpec with MockFactory {

  val request = CreateUserRequest("martynha", "Martina", "Magnani")
  val expectedUser = User("martynha", "Martina", "Magnani", "", true, new Date())

  val threadExecutor = mock[ThreadExecutor]
  val postExecutionThread = mock[PostExecutionThread]
  val userRepository = mock[UserRepository]
  val subscriber = mock[Subscriber[User]]

  val createUserUseCase = new CreateUserUseCase(threadExecutor, postExecutionThread, userRepository)

  "CreateUserUseCase" should "call UserRepository.createUser with 'request' ad parameter, and it returns an Observable of User equal to 'expectedUser'" in  {

    //given
    //expectations
    (userRepository createUser _).expects(request).returning(Observable.just(expectedUser))

    //when
    createUserUseCase(request, Subscriber())

    //then
    // UserRepository should be called with 'request' as parameter and returns an Observable of User equal to 'expectedUser'
    (subscriber onNext _ ).verify(expectedUser).once
  }
}
