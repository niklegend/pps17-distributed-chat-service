package it.unibo.dcs.service.webapp.repositories.datastores.api

import it.unibo.dcs.service.webapp.model.User
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import rx.lang.scala.Observable

trait UserApi {
  def createUser(request: RegisterUserRequest): Observable[User]

  def getUserByUsername(username: String): Observable[User]
}
