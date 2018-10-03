package it.unibo.dcs.authentication_service.common

import io.vertx.scala.ext.sql.SQLConnection
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import rx.lang.scala.Subscriber

class AuthenticationDataStoreTest extends FlatSpec with MockFactory {

  val username = "ale"
  val password = "123"
  val sqlConnection: SQLConnection = stub[SQLConnection]
  val authDataStore: AuthenticationDataStore = new AuthenticationDataStoreDatabase(sqlConnection)
  val logoutSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]

  it should "login the user when the loginUser is called" in {
    val loginSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
    authDataStore loginUser(username, password) subscribe loginSubscriber

    (() => loginSubscriber onCompleted) verify() once()
  }

  it should "logout the user when logoutUser is called" in {
    authDataStore logoutUser username subscribe logoutSubscriber

    (() => logoutSubscriber onCompleted) verify() once()
  }

  it should "register the user when createUser is called" in {
    val registerSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
    authDataStore createUser(username, password) subscribe registerSubscriber

    (() => registerSubscriber onCompleted) verify() once()
  }

}