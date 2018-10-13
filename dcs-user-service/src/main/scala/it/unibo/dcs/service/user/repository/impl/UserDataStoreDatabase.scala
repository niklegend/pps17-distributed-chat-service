package it.unibo.dcs.service.user.repository.impl

import io.vertx.lang.scala.json.{JsonArray, JsonObject}
import io.vertx.scala.ext.sql.SQLConnection
import it.unibo.dcs.commons.dataaccess.Implicits.{dateToString, _}
import it.unibo.dcs.commons.dataaccess.{DataStoreDatabase, ResultSetHelper}
import it.unibo.dcs.service.user.model.User
import it.unibo.dcs.service.user.model.exception.UserNotFoundException
import it.unibo.dcs.service.user.repository.UserDataStore
import it.unibo.dcs.service.user.repository.impl.UserDataStoreDatabase.Implicits._
import it.unibo.dcs.service.user.repository.impl.UserDataStoreDatabase._
import it.unibo.dcs.service.user.request.{CreateUserRequest, GetUserRequest}
import rx.lang.scala.Observable

final class UserDataStoreDatabase(connection: SQLConnection) extends DataStoreDatabase(connection) with UserDataStore {

  override def getUserByUsername(request: GetUserRequest): Observable[User] =
    query(selectUserByUsername, request)
      .map { resultSet =>
        if (resultSet.getResults.isEmpty) {
          throw UserNotFoundException(request.username)
        } else {
          ResultSetHelper.getRows(resultSet).head
        }
      }

  override def createUser(request: CreateUserRequest): Observable[User] =
    update(insertUser, request)
      .flatMap(_ => getUserByUsername(GetUserRequest(request.username)))

}

private[impl] object UserDataStoreDatabase {

  val selectUserByUsername = "SELECT * FROM `users` WHERE `username` = ?"

  val insertUser = "INSERT INTO `users` (`username`, `first_name`, `last_name`) VALUES (?, ?)"

  object Implicits {

    implicit def requestToParams(request: CreateUserRequest): JsonArray =
      new JsonArray().add(request.username).add(request.firstName).add(request.lastName)

    implicit def requestToParams(request: GetUserRequest): JsonArray =
      new JsonArray().add(request.username)

    implicit def jsonObjectToUser(userJsonObject: JsonObject): User = {
      User(userJsonObject.getString("username"),
        userJsonObject.getString("first_name"),
        userJsonObject.getString("last_name"),
        userJsonObject.getString("bio"),
        userJsonObject.getString("visible"),
        userJsonObject.getString("last_seen"))
    }

    implicit def userToJsonObject(user: User): JsonObject = {
      new JsonObject()
        .put("username", user.username)
        .put("first_name", user.firstName)
        .put("last_name", user.lastName)
        .put("bio", user.bio)
        .put("visible", booleanToString(user.visible))
        .put("last_seen", dateToString(user.lastSeen))
    }

  }

}
