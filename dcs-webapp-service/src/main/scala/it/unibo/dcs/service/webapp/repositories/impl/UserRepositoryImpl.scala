package it.unibo.dcs.service.webapp.repositories.impl

import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.{Requests, UserRepository}
import rx.lang.scala.Observable

class UserRepositoryImpl extends UserRepository {
  override def getUserByUsername(username: String): Observable[User] = ???

  override def registerUser(request: Requests.RegisterUserRequest): Observable[User] = ???
}
