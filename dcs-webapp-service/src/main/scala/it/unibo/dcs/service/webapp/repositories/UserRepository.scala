package it.unibo.dcs.service.webapp.repositories

import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import rx.lang.scala.Observable

trait UserRepository {
  def getUserByUsername(username: String): Observable[User]

  def registerUser(request: RegisterUserRequest): Observable[User]
}
