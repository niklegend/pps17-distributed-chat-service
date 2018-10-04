package it.unibo.dcs.service.webapp.repositories.datastores

import it.unibo.dcs.service.webapp.repositories.Requests.{LoginUserRequest, RegisterUserRequest}
import rx.lang.scala.Observable

trait AuthenticationDataStore {
  def loginUser(request: LoginUserRequest): Observable[String]

  def registerUser(request: RegisterUserRequest): Observable[String]

  def logoutUser(username: String): Observable[Unit]
}
