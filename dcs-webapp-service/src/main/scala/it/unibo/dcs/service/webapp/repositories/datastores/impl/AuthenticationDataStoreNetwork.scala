package it.unibo.dcs.service.webapp.repositories.datastores.impl

<<<<<<< HEAD
import it.unibo.dcs.service.webapp.interaction.Requests
import it.unibo.dcs.service.webapp.interaction.Requests.LoginUserRequest
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import rx.lang.scala.Observable

class AuthenticationDataStoreNetwork(private val authenticationApi: AuthenticationApi) extends AuthenticationDataStore {

  override def loginUser(loginUserRequest: LoginUserRequest): Observable[String] = authenticationApi.loginUser(loginUserRequest)

  override def registerUser(request: Requests.RegisterUserRequest): Observable[String] = authenticationApi.registerUser(request)

  override def logoutUser(username: String): Observable[Unit] = authenticationApi.logoutUser(username)
=======
import it.unibo.dcs.service.webapp.repositories.Requests
import it.unibo.dcs.service.webapp.repositories.datastores.AuthenticationDataStore
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import rx.Single

class AuthenticationDataStoreNetwork(private val authenticationApi: AuthenticationApi) extends AuthenticationDataStore {

  override def loginUser(username: String, password: String): Single[Boolean] = authenticationApi.loginUser(username, password)

  override def registerUser(request: Requests.RegisterUserRequest): Single[Boolean] = authenticationApi.registerUser(request)

  override def logoutUser(username: String): Single[Boolean] = authenticationApi.logoutUser(username)
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
}
