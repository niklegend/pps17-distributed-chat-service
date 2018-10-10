package it.unibo.dcs.service.webapp.repositories

import it.unibo.dcs.service.webapp.interaction.Requests.{CreateRoomRequest, LoginUserRequest, RegisterUserRequest}
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import it.unibo.dcs.service.webapp.repositories.impl.AuthenticationRepositoryImpl
import rx.lang.scala.Observable

trait AuthenticationRepository {
  def loginUser(request: LoginUserRequest): Observable[String]

  def registerUser(request: RegisterUserRequest): Observable[String]

  def logoutUser(username: String): Observable[Unit]

  def createRoom(request: CreateRoomRequest): Observable[String]
}

object AuthenticationRepository {
  def apply(authenticationDataStore: AuthenticationDataStore): AuthenticationRepository =
    new AuthenticationRepositoryImpl(authenticationDataStore)
}
