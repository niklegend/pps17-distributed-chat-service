package it.unibo.dcs.authentication_service.common

import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import rx.lang.scala.{Observable, Subscriber}

class AuthenticationRepositoryTest extends FlatSpec with MockFactory {

  val username = "ale"
  val password = "123"
  val expectedLoginRegisterResult = "token"
  val expectedLogoutResult: Unit = Unit
  val authDataStore: AuthenticationDataStore = mock[AuthenticationDataStore]
  val authRepository = new AuthenticationRepositoryImpl(authDataStore)
  val logoutSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]

  it should "login the user when the loginUser is called" in {
    val loginSubscriber: Subscriber[String] = stub[Subscriber[String]]
    (authDataStore loginUser(_, _)) expects (username, password) returns (Observable just expectedLoginRegisterResult)

    authRepository loginUser(username, password) subscribe loginSubscriber

    (loginSubscriber onNext _) verify expectedLoginRegisterResult once()
    (() => loginSubscriber onCompleted) verify() once()
  }

  it should "logout the user when logoutUser is called" in {
    (authDataStore logoutUser _) expects username returns (Observable just expectedLogoutResult)

    authRepository logoutUser username subscribe logoutSubscriber

    (logoutSubscriber onNext _) verify expectedLogoutResult once()
    (() => logoutSubscriber onCompleted) verify() once()
  }

  it should "register the user when createUser is called" in {
    val registerSubscriber: Subscriber[String] = stub[Subscriber[String]]
    (authDataStore createUser(_, _)) expects (username, password) returns (Observable just expectedLoginRegisterResult)

    authRepository createUser(username, password) subscribe registerSubscriber

    (registerSubscriber onNext _) verify expectedLoginRegisterResult once()
    (() => registerSubscriber onCompleted) verify() once()
  }

}