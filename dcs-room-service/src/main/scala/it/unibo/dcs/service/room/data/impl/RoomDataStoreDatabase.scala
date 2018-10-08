package it.unibo.dcs.service.room.data.impl

import io.vertx.lang.scala.json.JsonObject
import io.vertx.scala.ext.sql.SQLConnection
import it.unibo.dcs.commons.VertxHelper

import it.unibo.dcs.commons.VertxHelper.Implicits._
import it.unibo.dcs.commons.dataaccess.{DataStoreDatabase, InsertParams}
import it.unibo.dcs.service.room.data.RoomDataStore
import it.unibo.dcs.service.room.data.impl.RoomDataStoreDatabase.Implicits._
import it.unibo.dcs.service.room.request.{CreateUserRequest, DeleteRoomRequest}
import rx.lang.scala.Observable

class RoomDataStoreDatabase(protected override val connection: SQLConnection) extends DataStoreDatabase with RoomDataStore {

  override def createUser(request: CreateUserRequest): Observable[Unit] = insert("users", request)

  override def deleteRoom(request: DeleteRoomRequest): Observable[Unit] =
    VertxHelper.toObservable[Unit] {
      connection.execute(s"DELETE FROM `rooms` WHERE `name` = '${request.name}' AND `owner_username` = '${request.username}';", _)
    }

}

object RoomDataStoreDatabase {

  private def requestToJsonObject(request: CreateUserRequest): JsonObject = {
    new JsonObject().put("username", request.username)
  }

  object Implicits {

    implicit def requestToInsertParams(request: CreateUserRequest): InsertParams = {
      InsertParams(requestToJsonObject(request))
    }

  }

}
