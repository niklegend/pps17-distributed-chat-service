package it.unibo.dcs.service.webapp.repositories

import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import rx.Single

trait AuthenticationApi {
  def loginUser(username: String, password: String): Single[Boolean]

  def registerUser(request: RegisterUserRequest): Single[Boolean]

  def logoutUser(username: String): Single[Boolean]
}
