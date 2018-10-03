package it.unibo.dcs.service.room.interactor.repository

import it.unibo.dcs.service.room.data.RoomDataSource
import it.unibo.dcs.service.room.repository.impl.RoomRepositoryImpl
import it.unibo.dcs.service.room.request.CreateUserRequest
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, OneInstancePerTest}
import rx.lang.scala.{Observable, Subscriber}

final class UserRepositorySpec extends FlatSpec with MockFactory with OneInstancePerTest {

  val roomDataSource = mock[RoomDataSource]
  val roomRepository = new RoomRepositoryImpl(roomDataSource)

  val subscriber = stub[Subscriber[Unit]]

  val request = CreateUserRequest("mvandi")

  it should "create a new user" in {
    // Given
    (roomRepository createUser _) expects request returns Observable.just()

    // When
    roomRepository.createUser(request).subscribe(subscriber)

    // Then
    (subscriber.onCompleted: () => Unit) verify() once()
  }

}
