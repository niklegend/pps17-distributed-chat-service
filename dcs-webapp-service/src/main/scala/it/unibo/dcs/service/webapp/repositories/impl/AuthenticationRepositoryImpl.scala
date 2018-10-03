package it.unibo.dcs.service.webapp.repositories.impl

import it.unibo.dcs.service.webapp.repositories.{AuthenticationRepository, Requests}
import rx.Single

class AuthenticationRepositoryImpl extends AuthenticationRepository {
  override def loginUser(username: String, password: String): Single[Boolean] = ???

  override def registerUser(request: Requests.RegisterUserRequest): Single[Boolean] = ???

  override def logoutUser(username: String): Single[Boolean] = ???
}
