package it.unibo.dcs.authentication_service.data

import java.sql.Date
import java.time.LocalDateTime
import io.vertx.core.{AsyncResult, Future}
import io.vertx.lang.scala.json.Json
import io.vertx.scala.ext.sql.{ResultSet, SQLConnection}
import it.unibo.dcs.commons.VertxHelper
import it.unibo.dcs.commons.dataaccess.{DataStoreDatabase, InsertParams}
import rx.lang.scala.Observable

class AuthenticationDataStoreDatabase(protected val connection: SQLConnection)
  extends DataStoreDatabase with AuthenticationDataStore {

  override def createUser(username: String, password: String): Observable[Unit] =
    insert("users", InsertParams(Json.obj(("username", username), ("password", password))))

  override def checkUserExistence(username: String): Observable[Unit] =
    checkRecordPresence("users", ("username", username))

  override def checkUserCredentials(username: String, password: String): Observable[Unit] =
    checkRecordPresence("users", ("username", username), ("password", password))

  override def invalidToken(token: String, expirationDate: LocalDateTime): Observable[Unit] =
    insert("invalid_tokens", InsertParams(Json.obj(("token", "'" + token + "'"),
      ("expiration_date", Date.valueOf(expirationDate.toLocalDate).toString))))

  override def isTokenValid(token: String): Observable[Boolean] =
    checkResultSetSize("SELECT * FROM invalid_tokens WHERE token = '" + token + "'", 0)

  private def checkRecordPresence(table: String, parameters: (String, String)*): Observable[Unit] =
    VertxHelper.toObservable[Unit] { handler =>
      val query = "SELECT * FROM " + table + " WHERE " +
        parameters.map(param => param._1 + " = " + "'" + param._2 + "'")
          .fold("")((param1, param2) => param1 + " AND " + param2)
      val completeQuery = query.replaceFirst("AND", "")
      connection.query(completeQuery, handleQueryResult(_, handler))
    }

  private def handleQueryResult(result: AsyncResult[ResultSet], handler: AsyncResult[Unit] => Unit): Unit = {
    val resultFuture: Future[Unit] = Future.future()
    if (result.succeeded() && result.result().getResults.nonEmpty) {
      resultFuture.complete()
    } else {
      resultFuture.fail(result.cause())
    }
    handler(resultFuture)
  }

  private def checkResultSetSize(query: String, size: Int): Observable[Boolean] =
    VertxHelper.toObservable[Boolean] { handler =>
      connection.query(query, ar => {
        val result: Future[Boolean] = Future.future()
        if (ar.succeeded()) {
          result.complete(ar.result().getResults.size == size)
        } else {
          result.fail(ar.cause())
        }
        handler(result)
      })
    }
}

object AuthenticationDataStoreDatabase{
  def apply(connection: SQLConnection) = new AuthenticationDataStoreDatabase(connection)
}
