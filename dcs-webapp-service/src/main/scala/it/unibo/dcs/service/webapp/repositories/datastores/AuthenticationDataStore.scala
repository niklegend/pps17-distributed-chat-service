package it.unibo.dcs.service.webapp.repositories.datastores

import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import rx.Single

trait AuthenticationDataStore {
  def loginUser(username: String, password: String): Single[Boolean]

  def registerUser(request: RegisterUserRequest): Single[Boolean]

  def logoutUser(username: String): Single[Boolean]
}
