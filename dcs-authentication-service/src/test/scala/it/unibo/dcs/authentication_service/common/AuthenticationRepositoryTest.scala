package it.unibo.dcs.authentication_service.common

import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import rx.lang.scala.{Observable, Subscriber}

class AuthenticationRepositoryTest extends FlatSpec with MockFactory {

  val username = "ale"
  val password = "123"
  val expectedResult: Unit = Unit
  val authDataStore: AuthenticationDataStore = mock[AuthenticationDataStore]
  val authRepository = new AuthenticationRepositoryImpl(authDataStore)
  val logoutSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]

  it should "login the user when the loginUser is called" in {
    val loginSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
    (authDataStore loginUser(_, _)) expects (username, password) returns (Observable just expectedResult)

    authRepository loginUser(username, password) subscribe loginSubscriber

    (loginSubscriber onNext _) verify expectedResult once()
    (() => loginSubscriber onCompleted) verify() once()
  }

  it should "logout the user when logoutUser is called" in {
    (authDataStore logoutUser _) expects username returns (Observable just expectedResult)

    authRepository logoutUser username subscribe logoutSubscriber

    (logoutSubscriber onNext _) verify expectedResult once()
    (() => logoutSubscriber onCompleted) verify() once()
  }

  it should "register the user when createUser is called" in {
    val registerSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
    (authDataStore createUser(_, _)) expects (username, password) returns (Observable just expectedResult)

    authRepository createUser(username, password) subscribe registerSubscriber

    (registerSubscriber onNext _) verify expectedResult once()
    (() => registerSubscriber onCompleted) verify() once()
  }

}