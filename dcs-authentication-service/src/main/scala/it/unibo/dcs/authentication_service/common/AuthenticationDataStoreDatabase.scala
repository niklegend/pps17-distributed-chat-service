package it.unibo.dcs.authentication_service.common

import io.vertx.scala.ext.sql.SQLConnection
import it.unibo.dcs.commons.VertxHelper
import rx.lang.scala.Observable

class AuthenticationDataStoreDatabase(private[this] val connection: SQLConnection) extends AuthenticationDataStore {

  override def createUser(username: String, password: String): Observable[Unit] = {
    doesUserExist(username) flatMap(_ => insertUser(username, password))
  }

  private def insertUser(username: String, password: String): Observable[Unit] = {
    VertxHelper.toObservable[Unit] { handler =>
      connection.execute(s"INSERT INTO users(username, password) VALUES('$username', " + s"'$password')",
        handler(_))
    }
  }

  private def doesUserExist(username: String): Observable[Unit] = ???

  override def loginUser(username: String, password: String): Observable[Unit] = ???

  override def logoutUser(username: String): Observable[Unit] = ???

}
