package it.unibo.dcs.service.webapp.repositories.datastores.impl

import it.unibo.dcs.service.webapp.repositories.{AuthenticationRestApi, Requests}
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import rx.Single

class AuthenticationDataStoreNetwork(private val authenticationApi: AuthenticationRestApi) extends AuthenticationDataStore {

  override def loginUser(username: String, password: String): Single[Boolean] = ???

  override def registerUser(request: Requests.RegisterUserRequest): Single[Boolean] = ???

  override def logoutUser(username: String): Single[Boolean] = ???
}
