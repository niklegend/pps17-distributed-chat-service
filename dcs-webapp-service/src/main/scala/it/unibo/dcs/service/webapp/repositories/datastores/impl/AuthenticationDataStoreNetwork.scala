package it.unibo.dcs.service.webapp.repositories.datastores.impl

import it.unibo.dcs.service.webapp.repositories.Requests
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import rx.Single

class AuthenticationDataStoreNetwork(private val authenticationApi: AuthenticationApi) extends AuthenticationDataStore {

  override def loginUser(username: String, password: String): Single[Boolean] = authenticationApi.loginUser(username, password)

  override def registerUser(request: Requests.RegisterUserRequest): Single[Boolean] = authenticationApi.registerUser(request)

  override def logoutUser(username: String): Single[Boolean] = authenticationApi.logoutUser(username)
}
