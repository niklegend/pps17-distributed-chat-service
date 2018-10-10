package it.unibo.dcs.service.webapp.repositories.datastores

<<<<<<< HEAD
import it.unibo.dcs.service.webapp.interaction.Requests.{LoginUserRequest, RegisterUserRequest}
import it.unibo.dcs.service.webapp.repositories.datastores.api.AuthenticationApi
import it.unibo.dcs.service.webapp.repositories.datastores.impl.AuthenticationDataStoreNetwork
import rx.lang.scala.Observable

trait AuthenticationDataStore {

  def loginUser(request: LoginUserRequest): Observable[String]

  def registerUser(request: RegisterUserRequest): Observable[String]

  def logoutUser(username: String): Observable[Unit]

}

/* Companion object */
object AuthenticationDataStore {

  /* Factory method */
  def authDataStoreNetwork(authApi: AuthenticationApi): AuthenticationDataStore =
    new AuthenticationDataStoreNetwork(authApi)
=======
import it.unibo.dcs.service.webapp.repositories.Requests.RegisterUserRequest
import rx.Single

trait AuthenticationDataStore {
  def loginUser(username: String, password: String): Single[Boolean]

  def registerUser(request: RegisterUserRequest): Single[Boolean]

  def logoutUser(username: String): Single[Boolean]
>>>>>>> eb051361c76e4797646752817d00c27040a90d3f
}
