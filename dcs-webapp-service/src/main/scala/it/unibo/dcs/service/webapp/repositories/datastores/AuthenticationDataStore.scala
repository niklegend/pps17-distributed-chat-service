package it.unibo.dcs.service.webapp.repositories.datastores

import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import rx.lang.scala.Observable

trait AuthenticationDataStore {
  def loginUser(username: String, password: String): Observable[String]

  def registerUser(request: RegisterUserRequest): Observable[String]

  def logoutUser(username: String): Observable[Unit]
}
