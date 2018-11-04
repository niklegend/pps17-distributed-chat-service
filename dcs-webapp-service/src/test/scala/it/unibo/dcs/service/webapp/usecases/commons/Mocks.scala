package it.unibo.dcs.service.webapp.usecases.commons

import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, RoomRepository, UserRepository}
import org.scalamock.scalatest.MockFactory

object Mocks extends MockFactory {
  val roomRepository: RoomRepository = mock[RoomRepository]
  val authRepository: AuthenticationRepository = mock[AuthenticationRepository]
  val userRepository: UserRepository = mock[UserRepository]
}
