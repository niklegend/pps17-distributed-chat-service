package it.unibo.dcs.service.webapp.repositories.datastores

import it.unibo.dcs.service.webapp.repositories.datastores.api.RoomApi
import it.unibo.dcs.service.webapp.repositories.datastores.impl.RoomDataStoreNetwork
import rx.lang.scala.Observable

trait RoomDataStore {

  def deleteRoom(roomName: String, username: String): Observable[Unit]

}

object RoomDataStore {
  def roomDataStoreNetwork(roomApi: RoomApi): RoomDataStore = new RoomDataStoreNetwork(roomApi)
}