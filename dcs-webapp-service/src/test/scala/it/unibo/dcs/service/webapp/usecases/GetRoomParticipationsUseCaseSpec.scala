package it.unibo.dcs.service.webapp.usecases

import java.util.Date

import it.unibo.dcs.service.webapp.interaction.Requests.{CheckTokenRequest, GetRoomParticipationsRequest}
import it.unibo.dcs.service.webapp.interaction.Results.GetRoomParticipationsResult
import it.unibo.dcs.service.webapp.model.{Participation, Room, User}
import it.unibo.dcs.service.webapp.usecases.commons.Mocks._
import it.unibo.dcs.service.webapp.usecases.commons.UseCaseSpec
import rx.lang.scala.{Observable, Subscriber}

import scala.language.postfixOps

class GetRoomParticipationsUseCaseSpec extends UseCaseSpec {

  private val room = Room("AulaMagna")
  private val secondUser = User("mvandi", "Mattia", "Vandi", "bio", visible = true, new Date)
  private val firstParticipation = Participation(new Date(), room, user.username)
  private val secondParticipation = Participation(new Date(), room, secondUser.username)
  private val participations = Set(firstParticipation, secondParticipation)

  private val getRoomParticipationsRequest = GetRoomParticipationsRequest(room.name, user.username, token)

  private val checkTokenRequest = CheckTokenRequest(token, user.username)

  private val getRoomsParticipationsResult = GetRoomParticipationsResult(participations)

  private val getRoomParticipationsSubscriber: Subscriber[GetRoomParticipationsResult] =
    stub[Subscriber[GetRoomParticipationsResult]]

  private val getRoomParticipationsUseCase = new GetRoomParticipationsUseCase(threadExecutor, postExecutionThread,
    authRepository, roomRepository)

  it should "return all the participations for given room when the use case is executed" in {
    // Given
    (authRepository checkToken _) expects checkTokenRequest returns (Observable just token)
    (roomRepository getRoomParticipations _) expects getRoomParticipationsRequest returns
      (Observable just participations)

    // When
    // getRoomsUseCase is executed with argument `request`
    getRoomParticipationsUseCase(getRoomParticipationsRequest) subscribe getRoomParticipationsSubscriber

    // Then
    (getRoomParticipationsSubscriber onNext _) verify getRoomsParticipationsResult once()
    (() => getRoomParticipationsSubscriber onCompleted) verify() once()
  }

}
