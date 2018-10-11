package it.unibo.dcs.service.webapp.repositories.impl

import it.unibo.dcs.service.webapp.repositories.RoomRepository
import it.unibo.dcs.service.webapp.repositories.datastores.RoomDataStore
import rx.lang.scala.Observable

class RoomRepositoryImpl(roomDataStore: RoomDataStore) extends RoomRepository {

  override def deleteRoom(roomName: String, username: String): Observable[Unit] =
    roomDataStore.deleteRoom(roomName, username)
}
