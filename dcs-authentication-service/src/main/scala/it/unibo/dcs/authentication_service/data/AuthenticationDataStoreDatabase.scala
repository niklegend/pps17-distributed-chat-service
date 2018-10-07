package it.unibo.dcs.authentication_service.data

import java.sql.Date
import java.time.LocalDateTime
import io.vertx.core.Future
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.sql.SQLConnection
import it.unibo.dcs.commons.VertxHelper
import rx.lang.scala.Observable

class AuthenticationDataStoreDatabase(private[this] val connection: SQLConnection) extends AuthenticationDataStore {

  override def createUser(username: String, password: String): Observable[Unit] =
    runUnitQuery("INSERT INTO users(username, password) VALUES(?, ?)", username, password)

  override def checkUserExistence(username: String, password: String): Observable[Unit] =
    runUnitQuery("SELECT FROM users WHERE username = ? AND password = ?)", username, password)

  override def invalidToken(token: String, expirationDate: LocalDateTime): Observable[Unit] =
    runUnitQuery("INSERT INTO invalid_tokens(token, expiration_date) VALUES(?, ?)",
      token, Date.valueOf(expirationDate.toLocalDate).toString)

  override def isTokenInvalid(token: String): Observable[Boolean] =
    runBooleanQuery("SELECT FROM invalid_tokens WHERE token = ?", token)

  private def runUnitQuery(query: String, parameters: String*): Observable[Unit] = {
    val isSelectQuery = query.startsWith("SELECT") || query.startsWith("select")
    VertxHelper.toObservable[Unit] { handler =>
      connection.queryWithParams(query, Json.arr(parameters), ar => {
        val result: Future[Unit] = Future.future()
          if (ar.succeeded() && (!isSelectQuery || ar.result().getResults.nonEmpty)) {
            result.complete()
          } else {
            result.fail(ar.cause())
          }
        handler(result)
      }).close()
    }
  }

  private def runBooleanQuery(query: String, parameters: String*): Observable[Boolean] = {
    VertxHelper.toObservable[Boolean] { handler =>
      connection.queryWithParams(query, Json.arr(parameters), ar => {
        val result: Future[Boolean] = Future.future()
        if (ar.succeeded()) {
          result.complete(ar.result().getResults.nonEmpty)
        } else {
          result.fail(ar.cause())
        }
        handler(result)
      }).close()
    }
  }
}
