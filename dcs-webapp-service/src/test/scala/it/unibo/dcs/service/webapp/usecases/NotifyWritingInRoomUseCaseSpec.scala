package it.unibo.dcs.service.webapp.usecases

import it.unibo.dcs.service.webapp.interaction.Requests.CheckTokenRequest
import it.unibo.dcs.service.webapp.model.WritingUser
import it.unibo.dcs.service.webapp.usecases.commons.Mocks.{authRepository, postExecutionThread, threadExecutor}
import it.unibo.dcs.service.webapp.usecases.commons.UseCaseSpec
import rx.lang.scala.{Observable, Subscriber}

class NotifyWritingInRoomUseCaseSpec extends UseCaseSpec {

  private val writingUser = WritingUser("ale", "room1", "token")

  private val writingUserSubscriber = stub[Subscriber[Unit]]

  private val writingUserUseCase =
    new NotifyWritingInRoomUseCase(threadExecutor, postExecutionThread, authRepository)

  it should "check the token validity when it notifies the users in the room about a user's writing status" in {
    // Given
    // userRepository is called with `registerRequest` as parameter returns an observable that contains only `user`
    (authRepository checkToken _) expects CheckTokenRequest(token, writingUser.userName) returns (Observable just Unit)

    // When
    // sendMessageUseCase is executed with argument `request`
    writingUserUseCase(writingUser) subscribe writingUserSubscriber

    // Then
    (() => writingUserSubscriber onCompleted) verify() once()
  }
}
