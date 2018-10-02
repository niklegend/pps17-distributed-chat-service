package it.unibo.dcs.service.webapp.repositories.datastores.impl

import it.unibo.dcs.service.webapp.repositories.Requests
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import rx.lang.scala.Observable

class AuthenticationDataStoreNetwork(private val authenticationApi: AuthenticationApi) extends AuthenticationDataStore {

  override def loginUser(username: String, password: String): Observable[String] = authenticationApi.loginUser(username, password)

  override def registerUser(request: Requests.RegisterUserRequest): Observable[String] = authenticationApi.registerUser(request)

  override def logoutUser(username: String): Observable[Unit] = authenticationApi.logoutUser(username)
}
