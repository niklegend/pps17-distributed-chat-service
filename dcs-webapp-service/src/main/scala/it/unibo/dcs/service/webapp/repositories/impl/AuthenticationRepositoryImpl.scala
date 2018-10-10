package it.unibo.dcs.service.webapp.repositories.impl

<<<<<<< HEAD
import it.unibo.dcs.service.webapp.interaction.Requests
import it.unibo.dcs.service.webapp.repositories.AuthenticationRepository
import it.unibo.dcs.service.webapp.interaction.Requests.{LoginUserRequest, RegisterUserRequest}
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import rx.lang.scala.Observable


class AuthenticationRepositoryImpl(private val authenticationDataStore: AuthenticationDataStore)
  extends AuthenticationRepository {
  override def loginUser(loginUserRequest: LoginUserRequest): Observable[String] =
    authenticationDataStore.loginUser(loginUserRequest)

  override def registerUser(request: RegisterUserRequest): Observable[String] =
    authenticationDataStore.registerUser(request)

  override def logoutUser(username: String): Observable[Unit] = authenticationDataStore.logoutUser(username)

  override def createRoom(request: Requests.CreateRoomRequest): Observable[String] = ???
=======
import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, Requests}
import rx.Single

class AuthenticationRepositoryImpl extends AuthenticationRepository {
  override def loginUser(username: String, password: String): Single[Boolean] = ???

  override def registerUser(request: Requests.RegisterUserRequest): Single[Boolean] = ???

  override def logoutUser(username: String): Single[Boolean] = ???
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
}
