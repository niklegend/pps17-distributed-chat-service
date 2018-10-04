package it.unibo.dcs.authentication_service.common

import java.util.Date

import io.vertx.scala.ext.sql.SQLConnection
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import rx.lang.scala.Subscriber

class AuthenticationDataStoreTest extends FlatSpec with MockFactory {

  val username = "ale"
  val password = "123"
  val token = "token"
  val sqlConnection: SQLConnection = stub[SQLConnection]
  val authDataStore: AuthenticationDataStore = new AuthenticationDataStoreDatabase(sqlConnection)

  it should "find the user when checkUserExistence is called" in {
    val loginSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
    authDataStore checkUserExistence(username, password) subscribe loginSubscriber

    (() => loginSubscriber onCompleted) verify() once()
  }

  it should "invalid the token when invalidToken is called" in {
    val invalidateTokenSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
    authDataStore invalidToken (token, new Date()) subscribe invalidateTokenSubscriber

    (() => invalidateTokenSubscriber onCompleted) verify() once()
  }

  it should "register the user when createUser is called" in {
    val registerSubscriber: Subscriber[Unit] = stub[Subscriber[Unit]]
    authDataStore createUser(username, password) subscribe registerSubscriber

    (() => registerSubscriber onCompleted) verify() once()
  }

}