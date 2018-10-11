package it.unibo.dcs.service.webapp.repositories.datastores.impl

import it.unibo.dcs.service.webapp.repositories.datastores.RoomDataStore
import it.unibo.dcs.service.webapp.repositories.datastores.api.RoomApi
import rx.lang.scala.Observable

class RoomDataStoreNetwork(private val roomApi: RoomApi) extends RoomDataStore{

  override def deleteRoom(roomName: String, username: String): Observable[Unit] =
    roomApi.deleteRoom(roomName, username)
}
