package it.unibo.dcs.authentication_service.data

import java.time.LocalDateTime
import java.util.Date

import io.vertx.scala.ext.sql.SQLConnection
import it.unibo.dcs.authentication_service.data.{AuthenticationDataStore, AuthenticationDataStoreDatabase}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import rx.lang.scala.Subscriber

class AuthenticationDataStoreTest extends FlatSpec with MockFactory {

  val username = "ale"
  val password = "123"
  val token = "token"
  val sqlConnection: SQLConnection = mock[SQLConnection]
  val authDataStore: AuthenticationDataStore = new AuthenticationDataStoreDatabase(sqlConnection)

  it should "call sqlConnection and return a result when checkUserExistence is called" in {
    val loginSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
    specifyConnectionExpectation()
    authDataStore checkUserExistence(username, password) subscribe loginSubscriber

    assertSubscriber(loginSubscriber)
  }

  it should "call sqlConnection and return a result when invalidToken is called" in {
    val invalidateTokenSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
    specifyConnectionExpectation()
    authDataStore invalidToken (token, LocalDateTime.now()) subscribe invalidateTokenSubscriber

    assertSubscriber(invalidateTokenSubscriber)
  }

  it should "call sqlConnection and return a result when createUser is called" in {
    val registerSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
    specifyConnectionExpectation()
    authDataStore createUser(username, password) subscribe registerSubscriber

    assertSubscriber(registerSubscriber)
  }

  private def specifyConnectionExpectation(): Unit = {
    (sqlConnection queryWithParams (_, _, _)) expects (*, *, *) returns sqlConnection
    (() => sqlConnection close) expects () returns Unit
  }

  private def assertSubscriber(subscriber: Subscriber[_]): Unit = {
    (() => subscriber onStart) verify() once()
    (() => subscriber onCompleted) verify() once()
  }
}