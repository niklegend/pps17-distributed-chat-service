package it.unibo.dcs.authentication_service.common

import java.sql.Date
import java.time.LocalDateTime

import io.vertx.core.Future
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.sql.SQLConnection
import it.unibo.dcs.commons.VertxHelper
import rx.lang.scala.Observable

class AuthenticationDataStoreDatabase(private[this] val connection: SQLConnection) extends AuthenticationDataStore {

  override def createUser(username: String, password: String): Observable[Unit] =
    runQuery("INSERT INTO users(username, password) VALUES(?, ?)", username, password)

  override def checkUserExistence(username: String, password: String): Observable[Unit] =
    runQuery("SELECT FROM users WHERE username = ? AND password = ?)", username, password)

  override def invalidToken(token: String, expirationDate: LocalDateTime): Observable[Unit] =
    runQuery("INSERT INTO invalid_tokens(token, expiration_date) VALUES(?, ?)",
      token, Date.valueOf(expirationDate.toLocalDate).toString)

  private def runQuery(query: String, parameters: String*): Observable[Unit] = {
    VertxHelper.toObservable[Unit] { handler =>
      connection.queryWithParams(query, Json.arr(parameters), ar => {
        val result: Future[Unit] = Future.future()
          if (ar.succeeded()) {
            result.complete()
          } else {
            result.fail(ar.cause())
          }
        handler(result)
      }).close()
    }
  }
}
