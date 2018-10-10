package it.unibo.dcs.service.webapp.repositories

<<<<<<< HEAD
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
=======
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import rx.Single

trait AuthenticationRepository {
  def loginUser(username: String, password: String): Single[Boolean]

  def registerUser(request: RegisterUserRequest): Single[Boolean]

  def logoutUser(username: String): Single[Boolean]
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
}
