package it.unibo.dcs.service.room.interactor

import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import it.unibo.dcs.service.room.interactor.usecases.CreateRoomUseCase
import it.unibo.dcs.service.room.model.Room
import it.unibo.dcs.service.room.repository.RoomRepository
import it.unibo.dcs.service.room.request.CreateRoomRequest
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, OneInstancePerTest}
import rx.lang.scala.{Observable, Subscriber}

final class CreateRoomUseCaseSpec extends FlatSpec with MockFactory with OneInstancePerTest {
  val threadExecutor: ThreadExecutor = mock[ThreadExecutor]
  val postExecutionThread: PostExecutionThread = mock[PostExecutionThread]
  val roomRepository: RoomRepository = mock[RoomRepository]
  val createRoomUseCase = new CreateRoomUseCase(threadExecutor, postExecutionThread, roomRepository)

  val request = CreateRoomRequest("Aula Magna", "aulamagna")

  val subscriber: Subscriber[Room] = stub[Subscriber[Room]]

  it should "Create a room when the CreateRoomUseCase is execute" in {
    //Given
    (roomRepository createRoom _) expects request returns Observable.just()

    //When
    createRoomUseCase(request).subscribe(subscriber)

    //Then
    (subscriber.onCompleted: () => Unit) verify() once()
  }
}
