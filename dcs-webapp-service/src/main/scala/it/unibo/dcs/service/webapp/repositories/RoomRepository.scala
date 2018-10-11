package it.unibo.dcs.service.webapp.repositories

import it.unibo.dcs.service.webapp.repositories.datastores.RoomDataStore
import it.unibo.dcs.service.webapp.repositories.impl.RoomRepositoryImpl
import rx.lang.scala.Observable

trait RoomRepository {
  def deleteRoom(roomName: String, username: String): Observable[Unit]
}

object UserRepository {
  def apply(roomDataStore: RoomDataStore): RoomRepository = new RoomRepositoryImpl(roomDataStore)
}
