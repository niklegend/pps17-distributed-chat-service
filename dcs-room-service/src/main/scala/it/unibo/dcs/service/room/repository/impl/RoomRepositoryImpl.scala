package it.unibo.dcs.service.room.repository.impl

import it.unibo.dcs.service.room.data.RoomDataSource
import it.unibo.dcs.service.room.repository.RoomRepository
import it.unibo.dcs.service.room.request
import rx.lang.scala.Observable

final class RoomRepositoryImpl(private[this] val roomDataSource: RoomDataSource) extends RoomRepository {

  override def createUser(request: request.CreateUserRequest): Observable[Unit] = ???

}
